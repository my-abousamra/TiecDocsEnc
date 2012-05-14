package org;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {

    private String _zipFilePath = "";
    private String _imageFolderPath = "";
    private static final int BUFFER = 20480;
    private String _error = "";
    private File directory = null;
    private File files[] = null;
    static ArrayList<File> listOfFiles = new ArrayList<File>();
    FileOutputStream dest = null;
    ZipOutputStream zip = null;
    //--------
    long fileNumber = 0;

    public Zipper() {
    }

    public void setZipFilePath(String path) {
        _zipFilePath = path;
        try {
            dest = new FileOutputStream(_zipFilePath);
            zip = new ZipOutputStream(new BufferedOutputStream(dest));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void setImageFolderPath(String path) {
        _imageFolderPath = path;
        directory = new File(_imageFolderPath);
        listOfFiles.clear();
        listOfFiles.add(directory);
        files = directory.listFiles();
    }

    /**
     *
     * @return
     */
    public String getError() {
        return _error;
    }
    ArrayList<File> _files = null;

    /**
     *
     * @return
     */
    public long countFiles() {

        if (_files == null) {
            _files = new ArrayList<File>();
            _files.add(new File(_imageFolderPath));
        }
        if (_files.size() == 0) {
            _files = null;
            return 0;
        }
        File f = _files.get(0);
        _files.remove(0);
        if (f.isDirectory()) {
            for (File _file : f.listFiles()) {
                _files.add(_file);
            }
        } else {
            return 1 + countFiles();
        }

        return countFiles();
    }

    public long folder() {

        if (_files == null) {
            _files = new ArrayList<File>();
            _files.add(new File(_imageFolderPath));
        }
        if (_files.size() == 0) {
            _files = null;
            return 0;
        }
        File f = _files.get(0);
        _files.remove(0);
        if (f.isDirectory()) {
            for (File _file : f.listFiles()) {
                _files.add(_file);
            }
        } else {
            return 1 + countFiles();
        }

        return countFiles();
    }

    public void zipEm() {

        if (listOfFiles.size() == 0) {
            try {
                fileNumber = 0;
                zip.finish();
                zip.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        File f = listOfFiles.get(0);
        if (f.isDirectory()) {
            for (File _file : f.listFiles()) {
                listOfFiles.add(_file);
            }
        } else {
            fileNumber++;
            try {

                byte data[] = new byte[BUFFER];
                ZipEntry entry = null;

                int count;
                entry = new ZipEntry(f.getPath().replace(directory.getParentFile().getAbsolutePath(), ""));
                zip.putNextEntry(entry);
                // if(files[i].isDirectory())continue;

                FileInputStream fi = new FileInputStream(f);
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry fe = new ZipEntry(_error);

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    zip.write(data, 0, count);
                    zip.flush();
                }

                count = 0;
                origin.close();


            } catch (Exception e) {
                // handle exception
                _error = e.getMessage();
                e.printStackTrace();
            }
        }

        listOfFiles.remove(0);

        zipEm();
    }

    /**
     *
     */
    public void extractZipFile(String zipFileName, String extractedFileName) {

        try {
            String fSep = System.getProperty("file.separator");
            extractedFileName = extractedFileName.endsWith(fSep) ? extractedFileName : (extractedFileName + fSep);

            //Create input and output streams
            FileInputStream fis = new FileInputStream(zipFileName);
            ZipInputStream inStream = new ZipInputStream(fis);
            // OutputStream outStream = new FileOutputStream(extractedFileName);

            ZipEntry entry;
            byte[] buffer = new byte[1024];
            int nrBytesRead;
            int counter = 0;
            //Get next zip entry and start reading data
            while ((entry = inStream.getNextEntry()) != null) {
                File f = new File(extractedFileName + entry.getName());
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                }
                FileOutputStream out = new FileOutputStream(f);
                while ((nrBytesRead = inStream.read(buffer)) > 0) {
                    //outStream.write(buffer, 0, nrBytesRead);
                    out.write(buffer, 0, nrBytesRead);
                }
                out.close();
            }
            //Finish off by closing the streams
            //  outStream.close();
            inStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
