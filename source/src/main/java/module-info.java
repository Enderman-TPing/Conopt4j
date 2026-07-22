module io.github.et.reminder {
    requires org.apache.commons.io;
    requires org.apache.poi.ooxml;
    requires kotlin.stdlib;
    requires java.desktop;
    requires java.management;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires com.alibaba.fastjson2;
    requires kotlinx.coroutines.core.jvm;

    opens io.github.et.reminder to javafx.fxml;
    exports io.github.et.reminder;
    exports io.github.et.reminder.util;
    opens io.github.et.reminder.util to javafx.fxml;
}