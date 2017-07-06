package edu.galileo.android.flashcard.json;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SAMSUNG on 5/07/2017.
 */
public class JsonReader {

    public static String LoadJSONFromAsset(Context context, String filePath){
        String json;
        try{
            InputStream inputStream = context.getAssets().open(filePath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
        }catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
