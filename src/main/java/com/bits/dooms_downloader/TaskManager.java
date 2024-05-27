package com.bits.dooms_downloader;
import javax.swing.*;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 *
 * @author DoomDevil
 */
public class TaskManager {
  private ConcurrentLinkedQueue<SwingWorker<?, ?>> taskQueue = new ConcurrentLinkedQueue<>();
    private boolean isRunning = false;
    private JLabel taskInfoLabel;
    
    public TaskManager(JLabel taskInfoLabel){
        this.taskInfoLabel = taskInfoLabel;
    }

    public synchronized void addTask(SwingWorker<?, ?> task) {
        taskQueue.offer(task);
        updateTaskInfo();
        if (!isRunning) {
            executeNextTask();
        }
    }

    private synchronized void executeNextTask() {
        SwingWorker<?, ?> task = taskQueue.poll();
        if (task != null) {
            isRunning = true;
            updateTaskInfo();
            task.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    executeNextTask();
                }
            });
            task.execute();
        } else {
            isRunning = false;
            updateTaskInfo();
        }
    }
    
    private void updateTaskInfo() {
        SwingUtilities.invokeLater(() -> {
            StringBuilder info = new StringBuilder("<html>");
            if(isRunning && !taskQueue.isEmpty()){
                SwingWorker<?, ?> currentTask = taskQueue.peek();
                if(currentTask instanceof DownloadTask){
                    DownloadTask downloadTask = (DownloadTask) currentTask;
                    info.append("Descargando: ").append(downloadTask.getTaskInfo()).append("<pre>|<pre>");
                }
            }else{
                info.append("No hay descargas pendientes");
            }
            info.append("Pendiente: ").append(taskQueue.size()).append("<pre>");
            int count = 1;
            for(SwingWorker<?, ?> task : taskQueue){
                if(task instanceof DownloadTask){
                    DownloadTask downloadTask = (DownloadTask) task;
                    info.append("Descarga ").append(count++).append(": ").append(downloadTask.getTaskInfo()).append("<pre>");
                }else{
                    info.append("Descarga ").append(count++).append("Pendiente<pre>");
                }
            }
            info.append("</html>");
            taskInfoLabel.setText(info.toString());
        });
    }
}
