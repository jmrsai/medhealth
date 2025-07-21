package com.jmr.medhealth.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.StaticLayout
import android.text.TextPaint
import com.jmr.medhealth.data.local.Diagnosis
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {
    fun createPdf(context: Context, diagnosis: Diagnosis, image: Bitmap): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 page size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = TextPaint()

        paint.textSize = 24f
        canvas.drawText("MedHealth - Diagnosis Report", 20f, 40f, paint)

        paint.textSize = 14f
        canvas.drawText("Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(diagnosis.timestamp))}", 20f, 80f, paint)

        val scaledBitmap = Bitmap.createScaledBitmap(image, 300, 300, false)
        canvas.drawBitmap(scaledBitmap, (canvas.width - 300) / 2f, 120f, null)

        val yStart = 450f
        paint.textSize = 18f
        canvas.drawText("Result: ${diagnosis.result}", 20f, yStart, paint)
        canvas.drawText("Confidence: ${"%.2f".format(diagnosis.confidence * 100)}%", 20f, yStart + 30, paint)

        // Disclaimer
        paint.textSize = 10f
        val disclaimer = "Disclaimer: This is a preliminary analysis and not a substitute for professional medical advice. Consult a qualified ophthalmologist for an accurate diagnosis."
        val textLayout = StaticLayout.Builder.obtain(disclaimer, 0, disclaimer.length, paint, canvas.width - 40)
            .setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL)
            .build()
        canvas.save()
        canvas.translate(20f, yStart + 80)
        textLayout.draw(canvas)
        canvas.restore()

        pdfDocument.finishPage(page)

        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "MedHealth_Report_${diagnosis.timestamp}.pdf")
        try {
            FileOutputStream(file).use { pdfDocument.writeTo(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pdfDocument.close()
        return file
    }
}