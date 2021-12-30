package com.example.itextpdfandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TicketPdf extends AppCompatActivity {



    private void createPdf(String name,
                           String age,
                           String number,
                           String location) throws FileNotFoundException {
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

        File file = new File(pdfPath,"WaterTicket.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);

        Image bannerImage = createImage( getDrawable(R.drawable.banner_image));
        bannerImage.setHeight(90);

        Paragraph visitorTicket = new Paragraph("Visitor Ticket").setBold()
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER);
        Paragraph supplier = new Paragraph("Water Supply\nNarengi Tiniali , Guwahati")
                .setTextAlignment(TextAlignment.CENTER).setFontSize(12);

        Paragraph ray_bhawan = new Paragraph("Ray Bhawan").setBold()
                .setFontSize(20).setTextAlignment(TextAlignment.CENTER);


        DateTimeFormatter dateTimeFormatter=null,timeFormatter=null;
        String time="";
        String date="";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
            date = LocalDate.now().format(dateTimeFormatter).toString();
            time = LocalTime.now().format(timeFormatter).toString();
        }

        Table table = createTable(name,age,number, location,date,time);
        Image qrCode = createBarcode(pdfDocument,name, age, number, location,date, time);


        document.add(bannerImage);
        document.add(visitorTicket);
        document.add(supplier);
        document.add(ray_bhawan);
        document.add(table);
        document.add(qrCode);


        document.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();

        try {

            Bitmap bmp = generatePDFBitmap(file);

            //Source : https://stackoverflow.com/questions/11010386/passing-android-bitmap-data-within-activity-using-intent-in-android

            //Write file
            String filename = "bitmap.png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            bmp.recycle();

            //Pop intent
            Intent in1 = new Intent(this, PdfPreviewActivity.class);
            in1.putExtra("image", filename);
            startActivity(in1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Image createImage(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return  image;
    }

    Image createBarcode(PdfDocument pdfDocument,
                        String name,
                        String age,
                        String number,
                        String location,
                        String date,
                        String time){
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(name+"\n"+
                age+"\n"+
                number+"\n"+
                location+"\n"+
                date+"\n"+
                time);
        PdfFormXObject pdfFormXObject = barcodeQRCode.createFormXObject(ColorConstants.BLUE,pdfDocument);
        Image barcodeImage = new Image(pdfFormXObject).setWidth(60).setHorizontalAlignment(HorizontalAlignment.CENTER);



        return barcodeImage;
    }




    Table createTable(String name, String age, String number, String location, String date, String time){
        Border border = new GrooveBorder(5);
        float columnWidth[] = {100f,100f};
        Table table = new Table(columnWidth);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Visitor Name")));
        table.addCell(new Cell().add(new Paragraph(name)));

        table.addCell(new Cell().add(new Paragraph("Age")));
        table.addCell(new Cell().add(new Paragraph(age)));

        table.addCell(new Cell().add(new Paragraph("Mobile No.")));
        table.addCell(new Cell().add(new Paragraph(number)));

        table.addCell(new Cell().add(new Paragraph("Location")));
        table.addCell(new Cell().add(new Paragraph(location)));

        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph(date)));

        table.addCell(new Cell().add(new Paragraph("Time")));
        table.addCell(new Cell().add(new Paragraph(time)));

        return table;
    }


    private Bitmap generatePDFBitmap(File file) throws IOException {
        int WIDTH = 1440 ;
        int HEIGHT = 2392;
        ParcelFileDescriptor fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fd);
        Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        PdfRenderer.Page page = renderer.openPage(0);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        return bitmap;


    }


}
