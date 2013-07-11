/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.entity;

/*
 * Client-side (Cs) interface for classes holding TcpSynData
 */
public interface TcpSynDataCs {

    public int getIpId();

    public short getTos();

    public int getLength();

    public String getIpFlags();

    public short getTtl();

    public long getSequenceNum();
    
    public int getWindowSize();

    public String getTcpOptions();

    public String getTcpFlags();

    public int getTcpLength();
}
