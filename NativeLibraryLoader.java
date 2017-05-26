

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NativeLibraryLoader {

        public static boolean loadLibrary(Context context, String libPath, String libName) {

            File libs_dir = context.getDir("libs", Context.MODE_PRIVATE);

            AssetManager assetManager = context.getAssets();

            String new_file_name = libs_dir.getPath() + "/" + libName;
            try {
                File new_file = new File(new_file_name);
                if (new_file.exists())
                    ;
                else {
                    InputStream fis = assetManager.open(libName);

                    //String old_file_name = libPath + "/" + libName;
                    if (!copyLibrary(new_file, fis)) {
                        return false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                System.load(new_file_name);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }

            return true;
        }

        private static boolean copyLibrary(File new_file, InputStream fis) {

            //FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                //File new_file = new File(new_file_name);
                //File old_file = new File(old_file_name);

            /* delete old library if exists. */
                //if (new_file.exists())
                //    return true;
                //else
                //    new_file.delete();

                //fis = new FileInputStream(old_file);
                fos = new FileOutputStream(new_file);

                int     dataSize;
                byte[]  dataBuffer = new byte[2048];

                while ((dataSize = fis.read(dataBuffer)) != -1) {
                    fos.write(dataBuffer, 0, dataSize);
                }
                fos.flush();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (fis != null) fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

}
