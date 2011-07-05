package org.jenkinsci.plugins.fstrigger.triggers.filecontent;

import org.jenkinsci.plugins.fstrigger.FSTriggerException;
import org.jenkinsci.plugins.fstrigger.core.FSTriggerContentFileType;
import org.jenkinsci.plugins.fstrigger.service.FSTriggerLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class LogTextFileContentTest extends FileContentAbstractTest {

    FSTriggerContentFileType type;

    @Mock
    FSTriggerLog log;

    @Override
    public FSTriggerContentFileType getTypeInstance() {
        return type;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<LogTextFileContentEntry> expressions = new ArrayList<LogTextFileContentEntry>();
        expressions.add(new LogTextFileContentEntry("\\w*ERROR\\s*\\w*"));
        type = new LogTextFileContent(expressions);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNullFileRefAsInput() throws URISyntaxException, FSTriggerException {
        initType(null);
    }

    @Test(expected = FSTriggerException.class)
    public void testInitNoExistFileAsInput() throws URISyntaxException, FSTriggerException {
        File initFile = new File("noExist.txt");
        initType(initFile);
    }

    @Test(expected = FSTriggerException.class)
    public void testPollingNewFileNoExistAsInput() throws FSTriggerException, URISyntaxException {
        File initFile = new File(this.getClass().getResource("LogTextFileContent/initLog.txt").toURI());
        initType(initFile);
        type.isTriggeringBuild(new File("noExist"), log);
    }


    @Test(expected = NullPointerException.class)
    public void testPollingNewFileNullReferenceAsInput() throws FSTriggerException, URISyntaxException {
        File initFile = new File(this.getClass().getResource("LogTextFileContent/initLog.txt").toURI());
        initType(initFile);
        type.isTriggeringBuild(null, log);
    }

    @Test
    public void testPolling() throws URISyntaxException, FSTriggerException {
        File initFile = new File(this.getClass().getResource("LogTextFileContent/initLog.txt").toURI());
        initType(initFile);
        Assert.assertFalse(type.isTriggeringBuild(initFile, log));
        File newFile = new File(this.getClass().getResource("LogTextFileContent/newLog.txt").toURI());
        Assert.assertTrue(type.isTriggeringBuild(newFile, log));
    }
}
