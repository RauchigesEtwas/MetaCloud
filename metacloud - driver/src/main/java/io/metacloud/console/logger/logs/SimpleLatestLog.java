package io.metacloud.console.logger.logs;

import io.metacloud.Driver;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.group.GroupConfiguration;
import javafx.util.Pair;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class SimpleLatestLog {

    private File latestLog;
    private  List<String> header;
    private  LinkedList<String> logs;

    @SneakyThrows
    public SimpleLatestLog(){
        this.header = new ArrayList<>();
        this.logs = new LinkedList<>();

        if (!new File("./local/logs/").exists()){
            new File("./local/logs/").mkdirs();
        }
        if (new File("./local/logs/latest.log").exists()){

            getAllLogs().forEach(s -> {
                    if (!s.equalsIgnoreCase("latest.log"))      {
                        new File("./local/logs/" + s).delete();
                    }
            });

            InputStream is = null;
            OutputStream os = null;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                is = new FileInputStream(new File("./local/logs/latest.log"));
                os = new FileOutputStream(new File("./local/logs/Log_"+dtf.format(LocalDateTime.now())+".log"));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                is.close();
                os.close();
            }
            new File("./local/logs/latest.log").delete();
        }
        this.latestLog = new File("./local/logs/latest.log");
        this.latestLog.createNewFile();

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.latestLog), StandardCharsets.UTF_8), true);
        printWriter.flush();
        printWriter.close();

    }



    public void log( String line) {
        String lineToLog = line;
        lineToLog = lineToLog.replaceAll("\033\\[[;\\d]*m", "");
        this.logs.add(lineToLog);
        try {
            Writer output = new BufferedWriter(new FileWriter(this.latestLog, true));
            output.write(lineToLog);
            output.write("\n");
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveLogs(){
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.latestLog), StandardCharsets.UTF_8), true)) {
            for (String s : this.header)
                w.println(s);
            for (String loggedLine :this.logs) {
                if (loggedLine == null)
                    continue;
                w.println(loggedLine);
            }
            w.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private ArrayList<String> getAllLogs() {
        File file = new File("./local/logs/");
        File[] files = file.listFiles();
        ArrayList<String> logs = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            logs.add(FirstFilter);
        }
        return logs;
    }

}
