/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

//  http://nmap.org/book/osdetect-methods.html

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

//
//  Try this site when it's working again:
//      http://www.gomor.org/bin/view/Sinfp/SinfpDemo
//
@Entity
@Table(name="tcp_syn")
public class TcpSynData extends IpPacket implements Serializable {

    private long sequenceNum;
    private int windowSize;
    private String tcpOptions;
    private String tcpFlags;
    private int tcpLength;
    
    //private int maxSegmentSize;
    //private int windowScalingValue;
    //private boolean dontFragment;
    //private boolean sackOkFlag;
    //private boolean nopFlag;

    public long getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(long sequenceNum) {
        this.sequenceNum = sequenceNum;
    }
    
    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public String getTcpOptions() {
        return tcpOptions;
    }

    public void setTcpOptions(String tcpOptions) {
        this.tcpOptions = tcpOptions;
    }

    public String getTcpFlags() {
        return tcpFlags;
    }

    public void setTcpFlags(String tcpFlags) {
        this.tcpFlags = tcpFlags;
    }

    public int getTcpLength() {
        return tcpLength;
    }

    public void setTcpLength(int tcpLength) {
        this.tcpLength = tcpLength;
    }

    @Override
    public String toString() {
        return "SynPacket{" + "sequenceNum=" + sequenceNum + 
                ", windowSize=" + windowSize + ", tcpOptions=" + tcpOptions +
                ", tcpFlags=" + tcpFlags + 
                ", tcpLength=" + tcpLength + ", " + super.toString() + '}';
    }

    
}
