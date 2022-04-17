package io.metacloud.console.logger;

import io.metacloud.console.logger.enums.MSGType;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class LoggerOutputStream extends ByteArrayOutputStream {

    private final Logger logger;
    private final MSGType msqtype;

    public LoggerOutputStream(Logger logger, MSGType msqtype) {
        this.logger = logger;
        this.msqtype = msqtype;
    }

    @Override
    public void flush() {
        final var input = this.toString();
        this.reset();
        if (input != null && !input.isEmpty()) {
            String[] inputs = input.split("\n");
            for (String inputss : inputs)
            this.logger.log(this.msqtype, inputss);
        }
    }
}
