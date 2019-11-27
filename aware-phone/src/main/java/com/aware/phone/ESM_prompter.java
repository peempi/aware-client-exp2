package com.aware.phone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ESM;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Web;

import org.json.JSONException;

import java.util.Random;
import java.util.UUID;

public class ESM_prompter extends Service {

    public static int EXPIRATION_THRESHOLD =3000;
    public static String STUDY_URL ="https://api.awareframework.com/index.php/webservice/index/2488/pdsDIFdFzMwQ";

    public ESM_prompter() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("BBB", "Service on Start Command");

        String device_id = Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID);

        //URLs declaration
        String url1 = "https://gatech.co1.qualtrics.com/jfe/form/SV_8q6Lb9XYUMw4Tl3?device_id="+device_id;
        String url2 = "https://gatech.co1.qualtrics.com/jfe/form/SV_8q6Lb9XYUMw4Tl3?device_id="+device_id;
        String url3 = "https://gatech.co1.qualtrics.com/jfe/form/SV_8q6Lb9XYUMw4Tl3?device_id="+device_id;
        String url4 = "https://gatech.co1.qualtrics.com/jfe/form/SV_8q6Lb9XYUMw4Tl3?device_id="+device_id;
//        String url5 = "https://gatech.co1.qualtrics.com/jfe/form/SV_6ln2vFYdUuHbbH7?device_id="+device_id;
//        String url6 = "https://gatech.co1.qualtrics.com/jfe/form/SV_4OeytOYDJCpQYQZ?device_id="+device_id;
//        String url7 = "https://gatech.co1.qualtrics.com/jfe/form/SV_5t3hHBbYhTtgUFT?device_id="+device_id;
//        String url8 = "https://gatech.co1.qualtrics.com/jfe/form/SV_9nTTyy67jDTwKln?device_id="+device_id;

        String selectedurl;
//        String[] urls = {url1, url2, url3, url4, url5, url6};
        String[] urls = {url1, url2, url3, url4};
        int randomNumber = new Random().nextInt(urls.length);
        selectedurl = urls[randomNumber];
//        if (randomNumber < urls.length) {
//            selectedurl = urls[randomNumber];
//        } else {
//            randomNumber += new Random().nextInt(2);
//            selectedurl = (randomNumber == 0) ? url7 : url8;
//        }
        generateUrl(selectedurl, randomNumber);
/*
//        //I create the ESM
        Random rn = new Random();
        int value = rn.nextInt(4);
        Log.d("PIE", "Value = "+ value);
        switch (value){
            case 0:
                generateQuickAnswerESM();
                break;
            case 1:
                generateRadioESM();
                break;
            case 2:
                generateLikertESM();
                break;
            case 3:
                generateCheckboxESM();
                break;
        }
*/

        return super.onStartCommand(intent, flags, startId);
    }

    private void generateUrl(String survey_url, int index){
        Log.d("BBB", "Generate URL!!");
        try {
            ESMFactory factory = new ESMFactory();
            String uuid = UUID.randomUUID().toString();
            ESM_Web web = new ESM_Web();
//            web.setRefreshQueue(true);
            web.setURL(survey_url + "&uuid=" + uuid);
            web.setTitle("Web survey");
            web.setInstructions("Fill out this survey. Press exit button when finished");
            web.setUUID(uuid);
            web.setTimeGen(Long.toString(System.currentTimeMillis()));
//            ESM.setIndex(index);
            web.setNotificationTimeout(EXPIRATION_THRESHOLD);
//            web.setSubmitButton("OK");

            factory.addESM(web);

            ESM.queueESM(getApplicationContext(), factory.build());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
