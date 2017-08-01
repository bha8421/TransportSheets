package com.example.android.rawtodispatch.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.android.rawtodispatch.R;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.*;
/**
 * Created by Bharat on 7/28/2017.
 */

public class WriteService extends IntentService {

    private File directory=null;
    private File file=null;
    private HSSFWorkbook wb;
    public WriteService() {
        super("MyWriteService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(Calendar.getInstance().getTime());

        String[] values=intent.getStringArrayExtra("values");
        String sheetName=intent.getStringExtra("sheetName");

        String fileName=getResources().getString(R.string.app_name)+"-"+month_name+".xls";

        checkDirectory();

        checkFile(fileName);

        writeDataToExcel(values,sheetName);
    }

    private void writeDataToExcel(String[] values,String sheetName) {

        FileInputStream inputStream = null;
        POIFSFileSystem fileSystem = null;
        FileOutputStream outputStream=null;
        try {
            inputStream = new FileInputStream(file);
            fileSystem = new POIFSFileSystem(inputStream);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileSystem);

            HSSFSheet sheet = hssfWorkbook.getSheet(sheetName);


            Cell c = null;


            int next = sheet.getLastRowNum() + 1;
            //generate column heading
            Row row = sheet.createRow(next);


            for (int i=0;i<=values.length-1;i++){
                c=row.createCell(i);
                c.setCellValue(values[i]);
            }


            Toast.makeText(this, "last row number is: " + next, Toast.LENGTH_SHORT).show();

            outputStream = new FileOutputStream(file);
            hssfWorkbook.write(outputStream);
            Toast.makeText(this, "success: " + sheet.getLastRowNum(), Toast.LENGTH_SHORT).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void checkDirectory(){
        directory=new File(Environment.getExternalStorageDirectory()+"/RawToDispatch/");
        if(!directory.exists()) {
            directory.mkdir();
        }else {
            Log.i("<<Service>>","Directory exists");
        }

    }

    public void checkFile(String fileName) {
        file = new File(directory, fileName);
        if (!file.exists()) {
            wb = new HSSFWorkbook();




            createSheets(wb);


            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                wb.write(outputStream);
                wb.close();
                outputStream.close();
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.i("<<Service>>","File exists");
        }


    }
    public void createSheets(HSSFWorkbook wb) {
        Cell c = null;
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("raw");
        //generate column heading
        sheet(sheet1);
        sheet1=wb.createSheet("finish");
        sheet(sheet1);
        sheet1=wb.createSheet("dispatch");
        sheetDispatch(sheet1);

    }
    public void sheet(Sheet sheet){
        Cell c=null;
        Row row = sheet.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Vehicle Number");
        c = row.createCell(1);
        c.setCellValue("Date");
        c = row.createCell(2);
        c.setCellValue("Quantity");
        c=row.createCell(3);
        c.setCellValue("Receipt");
        c=row.createCell(4);
        c.setCellValue("Charges");
        c=row.createCell(5);
        c.setCellValue("Other Charges");

        sheet.setColumnWidth(0, (10 * 500));
        sheet.setColumnWidth(1, (10 * 500));
        sheet.setColumnWidth(2, (10 * 500));
        sheet.setColumnWidth(3, (10*  500));
        sheet.setColumnWidth(4, (10*  500));
        sheet.setColumnWidth(5, (10*  500));

    }
    public void sheetDispatch(Sheet sheet){
        Cell c=null;
        Row row = sheet.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Vehicle Number");
        c = row.createCell(1);
        c.setCellValue("Date");
        c = row.createCell(2);
        c.setCellValue("Quantity");
        c=row.createCell(3);
        c.setCellValue("Receipt");
        c=row.createCell(4);
        c.setCellValue("Charges");
        c=row.createCell(5);
        c.setCellValue("Other Charges");
        c=row.createCell(6);
        c.setCellValue("Source");
        c=row.createCell(7);
        c.setCellValue("Destination");


        sheet.setColumnWidth(0, (10 * 500));
        sheet.setColumnWidth(1, (10 * 500));
        sheet.setColumnWidth(2, (10 * 500));
        sheet.setColumnWidth(3, (10 * 500));
        sheet.setColumnWidth(4, (10 * 500));
        sheet.setColumnWidth(5, (10 * 500));
        sheet.setColumnWidth(6, (10 * 500));
        sheet.setColumnWidth(7, (10 * 500));

    }
}

