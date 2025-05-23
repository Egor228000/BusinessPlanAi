package com.example.businessplanai.viewModel

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.BusinessDao
import com.example.businessplanai.BusinessEnity
import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.ListItem
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.OutputStream
import java.io.StringReader
import javax.inject.Inject
import javax.xml.parsers.DocumentBuilderFactory



@HiltViewModel
class WatchViewModel @Inject constructor(private val dao: BusinessDao) : ViewModel() {



    private val _business = MutableStateFlow<BusinessEnity?>(null)
    val business: StateFlow<BusinessEnity?> = _business

    fun loadBusinessById(id: Int?) {
        viewModelScope.launch {
            val result = dao.getId(id)
            _business.value = result
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveTextToDownloads(context: Context, fileName: String, mimeType: String, content: String) {
        viewModelScope.launch {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val fileUri = resolver.insert(collection, contentValues)

            fileUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    when (mimeType) {
                        "application/pdf" -> writeMarkdownToPdfLikeDocx(
                            context,
                            outputStream,
                            content
                        )

                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> writeDocxWithMarkdownSimple(
                            outputStream,
                            content
                        )

                        else -> outputStream.write(content.toByteArray())
                    }
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
        }
    }

    fun writeDocxWithMarkdownSimple(outputStream: OutputStream, markdown: String) {
        // 1. Markdown → HTML (с поддержкой таблиц)
        val options = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        }
        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        val docMd = parser.parse(markdown)
        val html = renderer.render(docMd)

        // 2. HTML → DOM
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val wrappedHtml = "<body>$html</body>"
        val dom = builder.parse(InputSource(StringReader(wrappedHtml)))
        val body = dom.documentElement

        // 3. Собираем DOCX
        val doc = XWPFDocument()
        parseNode(body, doc)

        // 4. Сохраняем
        doc.write(outputStream)
        outputStream.flush()
        doc.close()
    }

    private fun cleanText(raw: String): String =
        raw.replace(Regex("[^\\p{Print}\\s]"), " ").trim()

    private fun parseNode(node: Node, doc: Document, font: PdfFont) {
        when (node.nodeName.lowercase()) {
            "p" -> {
                val para = Paragraph(cleanText(node.textContent)).setFont(font).setFontSize(12f)
                doc.add(para)
            }

            "strong", "b" -> {
                val para = Paragraph().add(
                    Text(cleanText(node.textContent)).setFont(font).setBold()
                ).setFontSize(12f)
                doc.add(para)
            }

            "em", "i" -> {
                val para = Paragraph().add(
                    Text(cleanText(node.textContent)).setFont(font).setItalic()
                ).setFontSize(12f)
                doc.add(para)
            }

            "ul" -> {
                val pdfList = com.itextpdf.layout.element.List()
                    .setSymbolIndent(12f)
                    .setListSymbol(Text("- ").setFont(font).setFontSize(12f))
                    .setFont(font)

                val items = node.childNodes
                for (i in 0 until items.length) {
                    val li = items.item(i)
                    if (li.nodeName.lowercase() == "li") {
                        val text = cleanText(li.textContent)
                        val listItem = ListItem(text).setFont(font).setFontSize(12f)
                        if (li.nodeName.lowercase() == "li") {
                            val text = cleanText(li.textContent)
                            val listItem = ListItem(text)
                            listItem.setFont(font)
                            listItem.setFontSize(12f)
                            pdfList.add(listItem)
                        }
                    }
                }

                doc.add(pdfList)
            }


            "table" -> {
                val trList = node.ownerDocument.getElementsByTagName("tr")
                var maxCols = 0
                // Определим максимальное количество колонок
                for (i in 0 until trList.length) {
                    val tr = trList.item(i)
                    if (tr.parentNode?.parentNode == node || tr.parentNode == node) {
                        val cells = tr.childNodes
                        var cols = 0
                        for (j in 0 until cells.length) {
                            if (cells.item(j).nodeName.lowercase() in listOf("td", "th")) {
                                cols++
                            }
                        }
                        maxCols = maxOf(maxCols, cols)
                    }
                }

                val table = Table(maxCols)
                table.setFont(font)
                for (i in 0 until trList.length) {
                    val tr = trList.item(i)
                    if (tr.parentNode?.parentNode == node || tr.parentNode == node) {
                        val cells = tr.childNodes
                        for (j in 0 until cells.length) {
                            val cellNode = cells.item(j)
                            if (cellNode.nodeName.lowercase() in listOf("td", "th")) {
                                val text = cleanText(cellNode.textContent)
                                val cell =
                                    Cell().add(Paragraph(text).setFont(font).setFontSize(12f))
                                table.addCell(cell)
                            }
                        }
                    }
                }
                doc.add(table)
            }

            "#text" -> {
                val text = cleanText(node.textContent)
                if (text.isNotEmpty()) {
                    val para = Paragraph(text).setFont(font).setFontSize(12f)
                    doc.add(para)
                }
            }

            else -> {
                // рекурсивный вызов
                val children = node.childNodes
                for (i in 0 until children.length) {
                    parseNode(children.item(i), doc, font)
                }
            }
        }
    }

    private fun parseNode(
        node: Node,
        doc: XWPFDocument,
        para: org.apache.poi.xwpf.usermodel.XWPFParagraph? = null
    ) {
        when (node.nodeName.lowercase()) {
            "p" -> {
                val paragraph = para ?: doc.createParagraph()
                val run = paragraph.createRun()
                run.fontFamily = "Arial"
                run.setText(cleanText(node.textContent))
            }

            "strong", "b" -> {
                val paragraph = para ?: doc.createParagraph()
                val run = paragraph.createRun()
                run.isBold = true
                run.fontFamily = "Arial"
                run.setText(cleanText(node.textContent))
            }

            "em", "i" -> {
                val paragraph = para ?: doc.createParagraph()
                val run = paragraph.createRun()
                run.isItalic = true
                run.fontFamily = "Arial"
                run.setText(cleanText(node.textContent))
            }

            "ul" -> {
                val items = node.childNodes
                for (i in 0 until items.length) {
                    val li = items.item(i)
                    if (li.nodeName.lowercase() == "li") {
                        val p = doc.createParagraph()
                        val run = p.createRun()
                        run.fontFamily = "Arial"
                        run.setText("• " + cleanText(li.textContent))
                    }
                }
            }

            "table" -> {
                // собираем все <tr> в любом месте под <table>
                val trList = node.ownerDocument
                    .getElementsByTagName("tr")
                val table = doc.createTable()
                for (i in 0 until trList.length) {
                    val rowNode = trList.item(i)
                    // пропускаем те tr, что вне текущего table
                    if (rowNode.parentNode?.parentNode == node || rowNode.parentNode == node) {
                        val row =
                            if (table.numberOfRows == 0) table.getRow(0) else table.createRow()
                        val cellNodes = rowNode.childNodes
                        var cellIndex = 0
                        for (j in 0 until cellNodes.length) {
                            val cellNode = cellNodes.item(j)
                            if (cellNode.nodeName.lowercase() in listOf("td", "th")) {
                                val cell =
                                    if (cellIndex == 0 && table.numberOfRows == 1 && row.tableCells.size == 1) {
                                        row.getCell(0)
                                    } else {
                                        row.createCell()
                                    }
                                // Взяли существующий параграф
                                val paragraph = cell.paragraphs.first()
                                // Удаляем все run-ы
                                while (paragraph.runs.isNotEmpty()) {
                                    paragraph.removeRun(0)
                                }
                                // Вставляем чистый текст
                                val run = paragraph.createRun()
                                run.fontFamily = "Arial"
                                run.setText(cleanText(cellNode.textContent))
                                cellIndex++
                            }
                        }
                    }
                }
            }

            "#text" -> {
                val text = cleanText(node.textContent)
                if (text.isNotEmpty()) {
                    val paragraph = para ?: doc.createParagraph()
                    val run = paragraph.createRun()
                    run.fontFamily = "Arial"
                    run.setText(text)
                }
            }

            else -> {
                // рекурсия по всем детям
                val children = node.childNodes
                for (i in 0 until children.length) {
                    parseNode(children.item(i), doc, para)
                }
            }
        }
    }


    fun writeMarkdownToPdfLikeDocx(context: Context, outputStream: OutputStream, markdown: String) {
        val options = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        }
        val parser = Parser.builder(options).build()
        val html = HtmlRenderer.builder(options).build().render(parser.parse(markdown))

        // 2. HTML → XML DOM
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val input = InputSource(StringReader("<body>$html</body>"))
        val htmlDoc = builder.parse(input)
        val root = htmlDoc.documentElement

        // 3. Создание PDF
        val writer = PdfWriter(outputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // 4. Настройка шрифта
        val fontBytes = context.assets.open("fonts/arialmt.ttf").readBytes()
        val fontProgram = FontProgramFactory.createFont(fontBytes)
        val font = PdfFontFactory.createFont(
            fontProgram,
            PdfEncodings.IDENTITY_H,
            PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
        )

        // 5. Построение PDF из DOM
        parseNode(root, document, font)

        // 6. Закрытие
        document.close()
    }

    fun getCurrentBusinessText(): String {
        val current = business.value
        return buildString {
            appendLine(current?.title ?: "")
            appendLine()
            appendLine(current?.description ?: "")
        }
    }
}