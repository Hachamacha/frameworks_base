/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
<<<<<<< HEAD
import android.util.LocalLog;
import android.util.Slog;

import com.google.android.collect.Lists;

import java.nio.charset.Charsets;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.LinkedList;

/**
 * Generic connector class for interfacing with a native daemon which uses the
 * {@code libsysutils} FrameworkListener protocol.
 */
final class NativeDaemonConnector implements Runnable, Handler.Callback, Watchdog.Monitor {
    private static final boolean LOGD = false;

    private final String TAG;

    private String mSocket;
    private OutputStream mOutputStream;
    private LocalLog mLocalLog;

    private final ResponseQueue mResponseQueue;

    private INativeDaemonConnectorCallbacks mCallbacks;
    private Handler mCallbackHandler;

    private AtomicInteger mSequenceNumber;

    private static final int DEFAULT_TIMEOUT = 1 * 60 * 1000; /* 1 minute */
    private static final long WARN_EXECUTE_DELAY_MS = 500; /* .5 sec */

    /** Lock held whenever communicating with native daemon. */
    private final Object mDaemonLock = new Object();

    private final int BUFFER_SIZE = 4096;

    NativeDaemonConnector(INativeDaemonConnectorCallbacks callbacks, String socket,
            int responseQueueSize, String logTag, int maxLogSize) {
        mCallbacks = callbacks;
        mSocket = socket;
        mResponseQueue = new ResponseQueue(responseQueueSize);
        mSequenceNumber = new AtomicInteger(0);
        TAG = logTag != null ? logTag : "NativeDaemonConnector";
        mLocalLog = new LocalLog(maxLogSize);
=======
import android.util.Slog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Generic connector class for interfacing with a native
 * daemon which uses the libsysutils FrameworkListener
 * protocol.
 */
final class NativeDaemonConnector implements Runnable, Handler.Callback, Watchdog.Monitor {
    private static final boolean LOCAL_LOGD = false;

    private BlockingQueue<String> mResponseQueue;
    private OutputStream          mOutputStream;
    private String                TAG = "NativeDaemonConnector";
    private String                mSocket;
    private INativeDaemonConnectorCallbacks mCallbacks;
    private Handler               mCallbackHandler;

    /** Lock held whenever communicating with native daemon. */
    private Object mDaemonLock = new Object();

    private final int BUFFER_SIZE = 4096;

    class ResponseCode {
        public static final int ActionInitiated                = 100;

        public static final int CommandOkay                    = 200;

        // The range of 400 -> 599 is reserved for cmd failures
        public static final int OperationFailed                = 400;
        public static final int CommandSyntaxError             = 500;
        public static final int CommandParameterError          = 501;

        public static final int UnsolicitedInformational       = 600;

        //
        public static final int FailedRangeStart               = 400;
        public static final int FailedRangeEnd                 = 599;
    }

    NativeDaemonConnector(INativeDaemonConnectorCallbacks callbacks,
                          String socket, int responseQueueSize, String logTag) {
        mCallbacks = callbacks;
        if (logTag != null)
            TAG = logTag;
        mSocket = socket;
        mResponseQueue = new LinkedBlockingQueue<String>(responseQueueSize);
>>>>>>> upstream/master
    }

    @Override
    public void run() {
        HandlerThread thread = new HandlerThread(TAG + ".CallbackHandler");
        thread.start();
        mCallbackHandler = new Handler(thread.getLooper(), this);

        while (true) {
            try {
                listenToSocket();
            } catch (Exception e) {
<<<<<<< HEAD
                loge("Error in NativeDaemonConnector: " + e);
=======
                Slog.e(TAG, "Error in NativeDaemonConnector", e);
>>>>>>> upstream/master
                SystemClock.sleep(5000);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        String event = (String) msg.obj;
        try {
<<<<<<< HEAD
            if (!mCallbacks.onEvent(msg.what, event, NativeDaemonEvent.unescapeArgs(event))) {
                log(String.format("Unhandled event '%s'", event));
            }
        } catch (Exception e) {
            loge("Error handling '" + event + "': " + e);
=======
            if (!mCallbacks.onEvent(msg.what, event, event.split(" "))) {
                Slog.w(TAG, String.format(
                        "Unhandled event '%s'", event));
            }
        } catch (Exception e) {
            Slog.e(TAG, String.format(
                    "Error handling '%s'", event), e);
>>>>>>> upstream/master
        }
        return true;
    }

    private void listenToSocket() throws IOException {
        LocalSocket socket = null;

        try {
            socket = new LocalSocket();
            LocalSocketAddress address = new LocalSocketAddress(mSocket,
                    LocalSocketAddress.Namespace.RESERVED);

            socket.connect(address);

            InputStream inputStream = socket.getInputStream();
<<<<<<< HEAD
            synchronized (mDaemonLock) {
                mOutputStream = socket.getOutputStream();
            }
=======
            mOutputStream = socket.getOutputStream();
>>>>>>> upstream/master

            mCallbacks.onDaemonConnected();

            byte[] buffer = new byte[BUFFER_SIZE];
            int start = 0;

            while (true) {
                int count = inputStream.read(buffer, start, BUFFER_SIZE - start);
<<<<<<< HEAD
                if (count < 0) {
                    loge("got " + count + " reading with start = " + start);
                    break;
                }
=======
                if (count < 0) break;
>>>>>>> upstream/master

                // Add our starting point to the count and reset the start.
                count += start;
                start = 0;

                for (int i = 0; i < count; i++) {
                    if (buffer[i] == 0) {
<<<<<<< HEAD
                        final String rawEvent = new String(
                                buffer, start, i - start, Charsets.UTF_8);
                        log("RCV <- {" + rawEvent + "}");

                        try {
                            final NativeDaemonEvent event = NativeDaemonEvent.parseRawEvent(
                                    rawEvent);
                            if (event.isClassUnsolicited()) {
                                // TODO: migrate to sending NativeDaemonEvent instances
                                mCallbackHandler.sendMessage(mCallbackHandler.obtainMessage(
                                        event.getCode(), event.getRawEvent()));
                            } else {
                                mResponseQueue.add(event.getCmdNumber(), event);
                            }
                        } catch (IllegalArgumentException e) {
                            log("Problem parsing message: " + rawEvent + " - " + e);
                        }

                        start = i + 1;
                    }
                }
                if (start == 0) {
                    final String rawEvent = new String(buffer, start, count, Charsets.UTF_8);
                    log("RCV incomplete <- {" + rawEvent + "}");
                }
=======
                        String event = new String(buffer, start, i - start);
                        if (LOCAL_LOGD) Slog.d(TAG, String.format("RCV <- {%s}", event));

                        String[] tokens = event.split(" ", 2);
                        try {
                            int code = Integer.parseInt(tokens[0]);

                            if (code >= ResponseCode.UnsolicitedInformational) {
                                mCallbackHandler.sendMessage(
                                        mCallbackHandler.obtainMessage(code, event));
                            } else {
                                try {
                                    mResponseQueue.put(event);
                                } catch (InterruptedException ex) {
                                    Slog.e(TAG, "Failed to put response onto queue", ex);
                                }
                            }
                        } catch (NumberFormatException nfe) {
                            Slog.w(TAG, String.format("Bad msg (%s)", event));
                        }
                        start = i + 1;
                    }
                }
>>>>>>> upstream/master

                // We should end at the amount we read. If not, compact then
                // buffer and read again.
                if (start != count) {
                    final int remaining = BUFFER_SIZE - start;
                    System.arraycopy(buffer, start, buffer, 0, remaining);
                    start = remaining;
                } else {
                    start = 0;
                }
            }
        } catch (IOException ex) {
<<<<<<< HEAD
            loge("Communications error: " + ex);
=======
            Slog.e(TAG, "Communications error", ex);
>>>>>>> upstream/master
            throw ex;
        } finally {
            synchronized (mDaemonLock) {
                if (mOutputStream != null) {
                    try {
<<<<<<< HEAD
                        loge("closing stream for " + mSocket);
                        mOutputStream.close();
                    } catch (IOException e) {
                        loge("Failed closing output stream: " + e);
=======
                        mOutputStream.close();
                    } catch (IOException e) {
                        Slog.w(TAG, "Failed closing output stream", e);
>>>>>>> upstream/master
                    }
                    mOutputStream = null;
                }
            }

            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
<<<<<<< HEAD
                loge("Failed closing socket: " + ex);
            }
        }
    }

    /**
     * Make command for daemon, escaping arguments as needed.
     */
    private void makeCommand(StringBuilder builder, String cmd, Object... args)
            throws NativeDaemonConnectorException {
        // TODO: eventually enforce that cmd doesn't contain arguments
        if (cmd.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("unexpected command: " + cmd);
        }

        builder.append(cmd);
        for (Object arg : args) {
            final String argString = String.valueOf(arg);
            if (argString.indexOf('\0') >= 0) {
                throw new IllegalArgumentException("unexpected argument: " + arg);
            }

            builder.append(' ');
            appendEscaped(builder, argString);
        }
    }

    /**
     * Issue the given command to the native daemon and return a single expected
     * response.
     *
     * @throws NativeDaemonConnectorException when problem communicating with
     *             native daemon, or if the response matches
     *             {@link NativeDaemonEvent#isClassClientError()} or
     *             {@link NativeDaemonEvent#isClassServerError()}.
     */
    public NativeDaemonEvent execute(Command cmd) throws NativeDaemonConnectorException {
        return execute(cmd.mCmd, cmd.mArguments.toArray());
    }

    /**
     * Issue the given command to the native daemon and return a single expected
     * response.
     *
     * @throws NativeDaemonConnectorException when problem communicating with
     *             native daemon, or if the response matches
     *             {@link NativeDaemonEvent#isClassClientError()} or
     *             {@link NativeDaemonEvent#isClassServerError()}.
     */
    public NativeDaemonEvent execute(String cmd, Object... args)
            throws NativeDaemonConnectorException {
        final NativeDaemonEvent[] events = executeForList(cmd, args);
        if (events.length != 1) {
            throw new NativeDaemonConnectorException(
                    "Expected exactly one response, but received " + events.length);
        }
        return events[0];
    }

    /**
     * Issue the given command to the native daemon and return any
     * {@link NativeDaemonEvent#isClassContinue()} responses, including the
     * final terminal response.
     *
     * @throws NativeDaemonConnectorException when problem communicating with
     *             native daemon, or if the response matches
     *             {@link NativeDaemonEvent#isClassClientError()} or
     *             {@link NativeDaemonEvent#isClassServerError()}.
     */
    public NativeDaemonEvent[] executeForList(Command cmd) throws NativeDaemonConnectorException {
        return executeForList(cmd.mCmd, cmd.mArguments.toArray());
    }

    /**
     * Issue the given command to the native daemon and return any
     * {@link NativeDaemonEvent#isClassContinue()} responses, including the
     * final terminal response.
     *
     * @throws NativeDaemonConnectorException when problem communicating with
     *             native daemon, or if the response matches
     *             {@link NativeDaemonEvent#isClassClientError()} or
     *             {@link NativeDaemonEvent#isClassServerError()}.
     */
    public NativeDaemonEvent[] executeForList(String cmd, Object... args)
            throws NativeDaemonConnectorException {
            return execute(DEFAULT_TIMEOUT, cmd, args);
    }

    /**
     * Issue the given command to the native daemon and return any
     * {@linke NativeDaemonEvent@isClassContinue()} responses, including the
     * final terminal response.  Note that the timeout does not count time in
     * deep sleep.
     *
     * @throws NativeDaemonConnectorException when problem communicating with
     *             native daemon, or if the response matches
     *             {@link NativeDaemonEvent#isClassClientError()} or
     *             {@link NativeDaemonEvent#isClassServerError()}.
     */
    public NativeDaemonEvent[] execute(int timeout, String cmd, Object... args)
            throws NativeDaemonConnectorException {
        final ArrayList<NativeDaemonEvent> events = Lists.newArrayList();

        final int sequenceNumber = mSequenceNumber.incrementAndGet();
        final StringBuilder cmdBuilder =
                new StringBuilder(Integer.toString(sequenceNumber)).append(' ');
        final long startTime = SystemClock.elapsedRealtime();

        makeCommand(cmdBuilder, cmd, args);

        final String logCmd = cmdBuilder.toString(); /* includes cmdNum, cmd, args */
        log("SND -> {" + logCmd + "}");

        cmdBuilder.append('\0');
        final String sentCmd = cmdBuilder.toString(); /* logCmd + \0 */

        synchronized (mDaemonLock) {
            if (mOutputStream == null) {
                throw new NativeDaemonConnectorException("missing output stream");
            } else {
                try {
                    mOutputStream.write(sentCmd.getBytes(Charsets.UTF_8));
                } catch (IOException e) {
                    throw new NativeDaemonConnectorException("problem sending command", e);
                }
            }
        }

        NativeDaemonEvent event = null;
        do {
            event = mResponseQueue.remove(sequenceNumber, timeout, sentCmd);
            if (event == null) {
                loge("timed-out waiting for response to " + logCmd);
                throw new NativeDaemonFailureException(logCmd, event);
            }
            log("RMV <- {" + event + "}");
            events.add(event);
        } while (event.isClassContinue());

        final long endTime = SystemClock.elapsedRealtime();
        if (endTime - startTime > WARN_EXECUTE_DELAY_MS) {
            loge("NDC Command {" + logCmd + "} took too long (" + (endTime - startTime) + "ms)");
        }

        if (event.isClassClientError()) {
            throw new NativeDaemonArgumentException(logCmd, event);
        }
        if (event.isClassServerError()) {
            throw new NativeDaemonFailureException(logCmd, event);
        }

        return events.toArray(new NativeDaemonEvent[events.size()]);
    }

    /**
     * Issue a command to the native daemon and return the raw responses.
     *
     * @deprecated callers should move to {@link #execute(String, Object...)}
     *             which returns parsed {@link NativeDaemonEvent}.
     */
    @Deprecated
    public ArrayList<String> doCommand(String cmd) throws NativeDaemonConnectorException {
        final ArrayList<String> rawEvents = Lists.newArrayList();
        final NativeDaemonEvent[] events = executeForList(cmd);
        for (NativeDaemonEvent event : events) {
            rawEvents.add(event.getRawEvent());
        }
        return rawEvents;
    }

    /**
     * Issues a list command and returns the cooked list of all
     * {@link NativeDaemonEvent#getMessage()} which match requested code.
     */
    @Deprecated
    public String[] doListCommand(String cmd, int expectedCode)
            throws NativeDaemonConnectorException {
        final ArrayList<String> list = Lists.newArrayList();

        final NativeDaemonEvent[] events = executeForList(cmd);
        for (int i = 0; i < events.length - 1; i++) {
            final NativeDaemonEvent event = events[i];
            final int code = event.getCode();
            if (code == expectedCode) {
                list.add(event.getMessage());
            } else {
                throw new NativeDaemonConnectorException(
                        "unexpected list response " + code + " instead of " + expectedCode);
            }
        }

        final NativeDaemonEvent finalEvent = events[events.length - 1];
        if (!finalEvent.isClassOk()) {
            throw new NativeDaemonConnectorException("unexpected final event: " + finalEvent);
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Append the given argument to {@link StringBuilder}, escaping as needed,
     * and surrounding with quotes when it contains spaces.
     */
    // @VisibleForTesting
    static void appendEscaped(StringBuilder builder, String arg) {
        final boolean hasSpaces = arg.indexOf(' ') >= 0;
        if (hasSpaces) {
            builder.append('"');
        }

        final int length = arg.length();
        for (int i = 0; i < length; i++) {
            final char c = arg.charAt(i);

            if (c == '"') {
                builder.append("\\\"");
            } else if (c == '\\') {
                builder.append("\\\\");
            } else {
                builder.append(c);
            }
        }

        if (hasSpaces) {
            builder.append('"');
        }
    }

    private static class NativeDaemonArgumentException extends NativeDaemonConnectorException {
        public NativeDaemonArgumentException(String command, NativeDaemonEvent event) {
            super(command, event);
        }

        @Override
        public IllegalArgumentException rethrowAsParcelableException() {
            throw new IllegalArgumentException(getMessage(), this);
        }
    }

    private static class NativeDaemonFailureException extends NativeDaemonConnectorException {
        public NativeDaemonFailureException(String command, NativeDaemonEvent event) {
            super(command, event);
        }
    }

    /**
     * Command builder that handles argument list building.
     */
    public static class Command {
        private String mCmd;
        private ArrayList<Object> mArguments = Lists.newArrayList();

        public Command(String cmd, Object... args) {
            mCmd = cmd;
            for (Object arg : args) {
                appendArg(arg);
            }
        }

        public Command appendArg(Object arg) {
            mArguments.add(arg);
            return this;
        }
    }

    /** {@inheritDoc} */
    public void monitor() {
        synchronized (mDaemonLock) { }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        mLocalLog.dump(fd, pw, args);
        pw.println();
        mResponseQueue.dump(fd, pw, args);
    }

    private void log(String logstring) {
        if (LOGD) Slog.d(TAG, logstring);
        mLocalLog.log(logstring);
    }

    private void loge(String logstring) {
        Slog.e(TAG, logstring);
        mLocalLog.log(logstring);
    }

    private static class ResponseQueue {

        private static class Response {
            public int cmdNum;
            public LinkedList<NativeDaemonEvent> responses = new LinkedList<NativeDaemonEvent>();
            public String request;
            public Response(int c, String r) {cmdNum = c; request = r;}
        }

        private final LinkedList<Response> mResponses;
        private int mMaxCount;

        ResponseQueue(int maxCount) {
            mResponses = new LinkedList<Response>();
            mMaxCount = maxCount;
        }

        public void add(int cmdNum, NativeDaemonEvent response) {
            Response found = null;
            synchronized (mResponses) {
                for (Response r : mResponses) {
                    if (r.cmdNum == cmdNum) {
                        found = r;
                        break;
                    }
                }
                if (found == null) {
                    // didn't find it - make sure our queue isn't too big before adding
                    // another..
                    while (mResponses.size() >= mMaxCount) {
                        Slog.e("NativeDaemonConnector.ResponseQueue",
                                "more buffered than allowed: " + mResponses.size() +
                                " >= " + mMaxCount);
                        // let any waiter timeout waiting for this
                        Response r = mResponses.remove();
                        Slog.e("NativeDaemonConnector.ResponseQueue",
                                "Removing request: " + r.request + " (" + r.cmdNum + ")");
                    }
                    found = new Response(cmdNum, null);
                    mResponses.add(found);
                }
                found.responses.add(response);
            }
            synchronized (found) {
                found.notify();
            }
        }

        // note that the timeout does not count time in deep sleep.  If you don't want
        // the device to sleep, hold a wakelock
        public NativeDaemonEvent remove(int cmdNum, int timeoutMs, String origCmd) {
            long endTime = SystemClock.uptimeMillis() + timeoutMs;
            long nowTime;
            Response found = null;
            while (true) {
                synchronized (mResponses) {
                    for (Response response : mResponses) {
                        if (response.cmdNum == cmdNum) {
                            found = response;
                            // how many response fragments are left
                            switch (response.responses.size()) {
                            case 0:  // haven't got any - must wait
                                break;
                            case 1:  // last one - remove this from the master list
                                mResponses.remove(response); // fall through
                            default: // take one and move on
                                response.request = origCmd;
                                return response.responses.remove();
                            }
                        }
                    }
                    nowTime = SystemClock.uptimeMillis();
                    if (endTime <= nowTime) {
                        Slog.e("NativeDaemonConnector.ResponseQueue",
                                "Timeout waiting for response");
                        return null;
                    }
                    /* pre-allocate so we have something unique to wait on */
                    if (found == null) {
                        found = new Response(cmdNum, origCmd);
                        mResponses.add(found);
                    }
                }
                try {
                    synchronized (found) {
                        found.wait(endTime - nowTime);
                    }
                } catch (InterruptedException e) {
                    // loop around to check if we're done or if it's time to stop waiting
                }
            }
        }

        public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
            pw.println("Pending requests:");
            synchronized (mResponses) {
                for (Response response : mResponses) {
                    pw.println("  Cmd " + response.cmdNum + " - " + response.request);
                }
            }
        }
=======
                Slog.w(TAG, "Failed closing socket", ex);
            }
        }
    }

    private void sendCommandLocked(String command) throws NativeDaemonConnectorException {
        sendCommandLocked(command, null);
    }

    /**
     * Sends a command to the daemon with a single argument
     *
     * @param command  The command to send to the daemon
     * @param argument The argument to send with the command (or null)
     */
    private void sendCommandLocked(String command, String argument)
            throws NativeDaemonConnectorException {
        if (command != null && command.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("unexpected command: " + command);
        }
        if (argument != null && argument.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("unexpected argument: " + argument);
        }

        if (LOCAL_LOGD) Slog.d(TAG, String.format("SND -> {%s} {%s}", command, argument));
        if (mOutputStream == null) {
            Slog.e(TAG, "No connection to daemon", new IllegalStateException());
            throw new NativeDaemonConnectorException("No output stream!");
        } else {
            StringBuilder builder = new StringBuilder(command);
            if (argument != null) {
                builder.append(argument);
            }
            builder.append('\0');

            try {
                mOutputStream.write(builder.toString().getBytes());
            } catch (IOException ex) {
                Slog.e(TAG, "IOException in sendCommand", ex);
            }
        }
    }

    /**
     * Issue a command to the native daemon and return the responses
     */
    public ArrayList<String> doCommand(String cmd) throws NativeDaemonConnectorException {
        synchronized (mDaemonLock) {
            return doCommandLocked(cmd);
        }
    }

    private ArrayList<String> doCommandLocked(String cmd) throws NativeDaemonConnectorException {
        mResponseQueue.clear();
        sendCommandLocked(cmd);

        ArrayList<String> response = new ArrayList<String>();
        boolean complete = false;
        int code = -1;

        while (!complete) {
            try {
                // TODO - this should not block forever
                String line = mResponseQueue.take();
                if (LOCAL_LOGD) Slog.d(TAG, String.format("RSP <- {%s}", line));
                String[] tokens = line.split(" ");
                try {
                    code = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException nfe) {
                    throw new NativeDaemonConnectorException(
                            String.format("Invalid response from daemon (%s)", line));
                }

                if ((code >= 200) && (code < 600)) {
                    complete = true;
                }
                response.add(line);
            } catch (InterruptedException ex) {
                Slog.e(TAG, "Failed to process response", ex);
            }
        }

        if (code >= ResponseCode.FailedRangeStart &&
                code <= ResponseCode.FailedRangeEnd) {
            /*
             * Note: The format of the last response in this case is
             *        "NNN <errmsg>"
             */
            throw new NativeDaemonConnectorException(
                    code, cmd, response.get(response.size()-1).substring(4));
        }
        return response;
    }

    /**
     * Issues a list command and returns the cooked list
     */
    public String[] doListCommand(String cmd, int expectedResponseCode)
            throws NativeDaemonConnectorException {

        ArrayList<String> rsp = doCommand(cmd);
        String[] rdata = new String[rsp.size()-1];
        int idx = 0;

        for (int i = 0; i < rsp.size(); i++) {
            String line = rsp.get(i);
            try {
                String[] tok = line.split(" ");
                int code = Integer.parseInt(tok[0]);
                if (code == expectedResponseCode) {
                    rdata[idx++] = line.substring(tok[0].length() + 1);
                } else if (code == NativeDaemonConnector.ResponseCode.CommandOkay) {
                    if (LOCAL_LOGD) Slog.d(TAG, String.format("List terminated with {%s}", line));
                    int last = rsp.size() -1;
                    if (i != last) {
                        Slog.w(TAG, String.format("Recv'd %d lines after end of list {%s}", (last-i), cmd));
                        for (int j = i; j <= last ; j++) {
                            Slog.w(TAG, String.format("ExtraData <%s>", rsp.get(i)));
                        }
                    }
                    return rdata;
                } else {
                    throw new NativeDaemonConnectorException(
                            String.format("Expected list response %d, but got %d",
                                    expectedResponseCode, code));
                }
            } catch (NumberFormatException nfe) {
                throw new NativeDaemonConnectorException(
                        String.format("Error reading code '%s'", line));
            }
        }
        throw new NativeDaemonConnectorException("Got an empty response");
    }

    /** {@inheritDoc} */
    public void monitor() {
        synchronized (mDaemonLock) { }
>>>>>>> upstream/master
    }
}
