package com.dreamdance.th.music;

/**
 * Author: B.S.
 * Date: 12-5-23
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/** AsyncTask for extracting a zip input stream to a directory */
public class UnzipStreamTask extends
        AsyncTask<InputStream, Integer, AsyncTaskResult<String>> {
    String unzipTo;

    public UnzipStreamTask(String unzipTo) {
        this.unzipTo = unzipTo;
    }

    @Override
    protected AsyncTaskResult<String> doInBackground(InputStream... streams) {
        ZipInputStream zis = null;
        AsyncTaskResult<String> result = null;
        try {

            File to = new File(unzipTo);
            to.mkdirs();

            int count = 0;

            InputStream is = streams[0];
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            if (zis.available() == 0) return new AsyncTaskResult<String>("ZipInputStream is not available.");
            while ((ze = zis.getNextEntry()) != null) {
                Log.v("UNZIP",
                        "Unzipping " + ze.getName());

                File f = new File(unzipTo + ze.getName());
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }

                if (ze.isDirectory()) {

                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                } else {
                    FileOutputStream fout = new FileOutputStream(unzipTo + ze.getName());

                    byte[] buffer = new byte[1024];
                    int c;

                    while ((c = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, c);
                    }

                    fout.close();
                }
            }

        } catch (Exception e) {
            //MobclickAgent.reportError(ctx, e.toString());
            result =  new AsyncTaskResult<String>(e);
        }

        if (null != zis) {
            try {
                zis.close();
            } catch (Exception e) {
                result =  new AsyncTaskResult<String>(e);
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        if (null == result) result = new AsyncTaskResult<String>(unzipTo);
        return result;
    }
}

