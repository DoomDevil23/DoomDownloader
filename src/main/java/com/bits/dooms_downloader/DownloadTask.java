package com.bits.dooms_downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.SwingUtilities;

/**
 * Execute file download in a background thread and update the progress.
 * @author DoomDevil
 *
 */
public class DownloadTask extends SwingWorker<Void, Void> {
    private static final int BUFFER_SIZE = 4096;
    private String downloadURL;
    private String saveDirectory;
    private TextOverlayProgressBar pgBar;
    private Queue<SwingWorker<?, ?>> workerQueue = new LinkedList<>();
    private String taskInfo;
    private boolean isExecuting = false;

    public DownloadTask(String downloadURL, String saveDirectory, TextOverlayProgressBar pgBar, String taskInfo) {
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
        this.pgBar = pgBar;
        this.taskInfo = taskInfo;
    }

    /**
     * Executed in background thread
     */
    @Override
    protected Void doInBackground() throws Exception {
        
        try {
            YouTube objYouTube = new YouTube();
            objYouTube.download_File(downloadURL);

            String saveFilePath = saveDirectory;

            InputStream inputStream = objYouTube.getInputStream();
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            long totalBytesRead = 0;
            int percentCompleted = 0;
            long fileSize = objYouTube.getContentLength();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                percentCompleted = (int) (totalBytesRead * 100 / fileSize);
                final int progress = percentCompleted;
                SwingUtilities.invokeLater(() -> {
                    pgBar.setValue(progress);
                    pgBar.setString(progress+"%");
                    pgBar.repaint();
                });
            }

            outputStream.close();

            objYouTube.disconnect();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al descargar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            //setProgress(0);
            pgBar.setValue(0);
            pgBar.setString("0%");
            pgBar.repaint();
            
            cancel(true);
        }
        return null;
    }
    
    public String getTaskInfo(){
        return taskInfo;
    }
    
    /**
     * Executed in Swing's event dispatching thread
     */
    @Override
    protected void done() {
        if (!isCancelled()) {
            JOptionPane.showMessageDialog(null, "Se ha Completado la descarga!", "Message", JOptionPane.INFORMATION_MESSAGE);
            pgBar.setValue(0);
            pgBar.setString("0%");
            pgBar.repaint();
            //executeNext();
        }
    }
}