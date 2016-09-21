package com.example.chari.mytest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
    }

    public void Download(View view)
    {

        new DownloadFile().execute("http://microblogging.wingnity.com/JSONParsingTutorial/brad.jpg", "brad.jpg");
       //new DownloadFile().execute("https://wiki.openoffice.org/w/images/1/1b/Testlink_user_manual.pdf","Testlink_user_manual.pdf");


    }

    public void DocView(View view)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/" + "brad.jpg");  // -> filename = maven.pdf
        Uri path=Uri.fromFile(pdfFile);
        // file name not be a image(1)...this is not supporting okk
        //Uri path = Uri.parse("file://"+"/storage/emulated/0/Download/images.jpg");
        //Uri path = Uri.parse("file://"+"/sdcard/images.jpg");
        // if somthing will be happen then you have to use content:// instead of file://

        Log.e("tag","path"+path);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
       // pdfIntent.setDataAndType(path, "image/*");
        pdfIntent.setDataAndType(path,"image/*");
        // for pdf you can use type "application/pdf"
        // for image "image/*"
        // for anything "*/*"

        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(Downloader.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }





    }


    private class DownloadFile extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            Log.e("tag","extStorageDirectory"+extStorageDirectory);
            File folder = new File(extStorageDirectory, "Download");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;        }
    }

    private static class FileDownloader
    {
        private static final int  MEGABYTE = 1024 * 1024;

        public static void downloadFile(String fileUrl, File directory){
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
               // urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();
                Log.e("tag","enter in size");

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
