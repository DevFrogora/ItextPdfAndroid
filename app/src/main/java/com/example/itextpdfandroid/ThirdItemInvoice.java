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
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

public class ThirdItemInvoice extends AppCompatActivity {


    EditText editTextName, editTextAge, editTextNumber, editTextLocation;
    Button submitButton;

    class ProductItem {
        int id;
        String name;
        int qty;
        int cost;
        int totalAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_layout);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextLocation = findViewById(R.id.editTextLocation);
        submitButton = findViewById(R.id.button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String number = editTextNumber.getText().toString();
                String location = editTextLocation.getText().toString();

                try {

                    HashMap<String, Object> invoiceData = new HashMap<>();
                    HashMap<String, Object> itemsData = new HashMap<>();
                    HashMap<String, Object> contactsData = new HashMap<>();
                    HashMap<String, Object> customerData = new HashMap<>();
                    HashMap<String, Object> paymentData = new HashMap<>();


                    // Adding values to HashMap as ("keys", "values")
                    customerData.put("name", "Ramu Prasad");
                    customerData.put("city", "Guwahati");
                    customerData.put("village", "Narengi Tinali ");
                    customerData.put("phone", "9954816690");

                    //payment Details
                    paymentData.put("userid", "Paypal: payments@watertanky.com");
                    paymentData.put("cardAccept", "MasterCard , Visa");

                    contactsData.put("phone", "995-499-7214");
                    contactsData.put("email", "ramu@gmail.com\nraju@gmail.com");
                    contactsData.put("location", "Narengi Tinali\n781026");

                    DateTimeFormatter dateTimeFormatter = null, timeFormatter = null;
                    String time = "";
                    String issueDate = "";
                    String dueDate = "";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
                        issueDate = LocalDate.now().format(dateTimeFormatter).toString();
                        dueDate = LocalDate.now().plusDays(3).format(dateTimeFormatter).toString();
                        time = LocalTime.now().format(timeFormatter).toString();
                    }

                    invoiceData.put("accountNumber", "9967836877");
                    invoiceData.put("invoiceDate", issueDate);


                    ProductItem productItem1 = new ProductItem();
                    ProductItem productItem2 = new ProductItem();
                    ProductItem productItem3 = new ProductItem();
                    ProductItem productItem4 = new ProductItem();
                    ProductItem[] productItems = new ProductItem[]{productItem1, productItem2,
                            productItem3, productItem4};

                    productItem1.id = 1;
                    productItem1.name = "Rice";
                    productItem1.qty = 1;
                    productItem1.cost = 100;
                    productItem1.totalAmount = productItem1.qty * productItem1.cost;

                    productItem2.id = 2;
                    productItem2.name = "Wheat";
                    productItem2.qty = 1;
                    productItem2.cost = 100;
                    productItem2.totalAmount = productItem2.qty * productItem2.cost;

                    productItem3.id = 3;
                    productItem3.name = "Onion";
                    productItem3.qty = 1;
                    productItem3.cost = 100;
                    productItem3.totalAmount = productItem3.qty * productItem3.cost;

                    productItem4.id = 4;
                    productItem4.name = "Potato";
                    productItem4.qty = 1;
                    productItem4.cost = 100;
                    productItem4.totalAmount = productItem4.qty * productItem4.cost;
                    Random random1 = new Random();
                    int invoiceNumber = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        invoiceNumber = random1.ints(1, 158220, 208220).findFirst().getAsInt();
                        ;
//                       //https://stackoverflow.com/a/48658684/10111806
                    }
                    itemsData.put("items", productItems);
                    invoiceData.put("invoiceNumber", invoiceNumber);

                    createPdf(invoiceData, itemsData, contactsData, customerData, paymentData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void createPdf(HashMap<String, Object> invoiceData, HashMap<String, Object> itemsData,
                           HashMap<String, Object> contactsData, HashMap<String, Object> customerData,
                           HashMap<String, Object> paymentData) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

        File file = new File(pdfPath, "ItemInvoice3.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        HashMap<String, Table> tables = createTable(invoiceData, itemsData, contactsData, customerData, paymentData);
        ;
        document.setMargins(0, 0, 0, 0);
        document.add(createImage(getDrawable(R.drawable.blue_background)).setFixedPosition(0, 0));
        document.add(createImage(getDrawable(R.drawable.water_banner)));
        document.add(tables.get("firstTable"));
        document.add(tables.get("secondTable"));


        document.add(new Paragraph("\n\n\n(Authorised Signatory)"));

        document.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();

        try {

            Bitmap bmp = generatePDFBitmap(file);

            //Source : https://stackoverflow.com/questions/11010386/passing-android-bitmap-data-within-activity-using-intent-in-android

            //Write file
            String filename = "invoiceItem2.png";
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

    HashMap<String, Table> createTable(HashMap<String, Object> invoiceData, HashMap<String, Object> itemsData,
                                       HashMap<String, Object> contactsData, HashMap<String, Object> customerData,
                                       HashMap<String, Object> paymentData) {

        float columnWidth1[] = {140, 140, 140, 140};
        Table table1 = new Table(columnWidth1);
        table1.setMargin(20);
        HashMap<String, Table> tables = new HashMap<>();
        tables.put("firstTable", table1);

        table1.addCell(new Cell().add(new Paragraph("Customer Name")
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(customerData.get("name").toString())
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Invoice No:")
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("#" + invoiceData.get("invoiceNumber")
        ).setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add(new Paragraph("Mo. No.")
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(customerData.get("phone").toString())
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("Date:")
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph(invoiceData.get("invoiceDate").toString())
                .setFontSize(14).setFontColor(ColorConstants.WHITE)).setBorder(Border.NO_BORDER));

        float columnWidth2[] = {120, 120, 100, 120};
        Table table2 = new Table(columnWidth2);
        table2.setMargin(20);

        ProductItem[] productItems = (ProductItem[]) itemsData.get("items");
        ProductItem productItem1 = productItems[0];
        ProductItem productItem2 = productItems[1];
        ProductItem productItem3 = productItems[2];
        ProductItem productItem4 = productItems[3];

        int subtotal = productItem1.totalAmount + productItem2.totalAmount
                + productItem3.totalAmount + productItem4.totalAmount;

        int discount = 0;

        int taxrate = 12;
        int tax = (subtotal / 100) * taxrate;

        int invoiceTotal = subtotal + tax;

        DeviceRgb tableColor = new DeviceRgb(8, 106, 119);
        table2.addCell(new Cell().add(new Paragraph("Item Description")
                .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(tableColor));
        table2.addCell(new Cell().add(new Paragraph("Price")
                .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(tableColor));
        table2.addCell(new Cell().add(new Paragraph("Qty")
                .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(tableColor));
        table2.addCell(new Cell().add(new Paragraph("Total")
                .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(tableColor));

        table2.addCell(new Cell().add(new Paragraph(productItem1.name)
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.cost))
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.qty))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem1.totalAmount))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));

        table2.addCell(new Cell().add(new Paragraph(productItem2.name)
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.cost))
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.qty))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem2.totalAmount))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));

        table2.addCell(new Cell().add(new Paragraph(productItem3.name)
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.cost))
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.qty))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem3.totalAmount))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));

        table2.addCell(new Cell().add(new Paragraph(productItem4.name)
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.cost))
                .setFontColor(ColorConstants.WHITE)).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.qty))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(productItem4.totalAmount))
                .setFontColor(ColorConstants.WHITE))
                .setBorder(new SolidBorder(tableColor, 1)));


        table2.addCell(new Cell(1, 2).add(new Paragraph("---Thanks!! Visit Again ---"))
                .setFontColor(ColorConstants.WHITE).setBorder(new SolidBorder(tableColor, 1)));
//        table2.addCell(new Cell().add(new Paragraph()));
        table2.addCell(new Cell().add(new Paragraph("Total"))
                .setBackgroundColor(tableColor)
                .setFontSize(16)
                .setFontColor(ColorConstants.WHITE).setBorder(new SolidBorder(tableColor, 1)));
        table2.addCell(new Cell().add(new Paragraph(String.valueOf(invoiceTotal)))
                .setBackgroundColor(tableColor)
                .setFontSize(16)
                .setFontColor(ColorConstants.WHITE).setBorder(new SolidBorder(tableColor, 1)));

        tables.put("secondTable", table2);


        return tables;
    }

    Image createImage(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return image;
    }


    private Bitmap generatePDFBitmap(File file) throws IOException {
        int WIDTH = 1440;
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