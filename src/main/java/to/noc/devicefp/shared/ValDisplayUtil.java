/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.shared;

import java.util.Date;

public class ValDisplayUtil {

    public static String nullToEmpty(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    public static String boolToEnabled(Boolean value) {
        if (value == null) {
            return "unknown";
        }
        return value ? "enabled" : "disabled";
    }
   
    public static String boolToYesNo(Boolean value) {
        if (value == null) {
            return "unknown";
        }
        return value ? "yes" : "no";
    }
    
    public static String boolToPassFail(Boolean value) {
        if (value == null) {
            return "unknown";
        }
        return value ? "passed" : "failed";
    }

    public static String boolToTrueFalse(Boolean value) {
        if (value == null) {
            return "unknown";
        }
        return value ? "true" : "false";
    }
    
    public static enum BoolType { YesNo, PassFail, EnabledDisabled, TrueFalse };
    
    public static String boolToString(Boolean value, BoolType type) {
        switch (type) {
            case YesNo:
                return boolToYesNo(value);
            case PassFail:
                return boolToPassFail(value);
            case EnabledDisabled:
                return boolToEnabled(value);
            case TrueFalse:
            default:
                return boolToTrueFalse(value);
        }
    }
    
    public static String emptyToNull(String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String getIntervalStr(Date start, Date end) {
        if (start == null || end == null) {
            return "-";
        }
        return getIntervalStr(end.getTime() - start.getTime());
    }
    
    public static String getIntervalStr(Integer milliseconds) {
        if (milliseconds == null) {
            return "-";
        }
        // C# craziness.  Environment.TickCount is stored in a signed
        // 32-bit integer that overflows.  We convert the value to
        // an unsigned 32-bit value below.
        //
        // (2^31) milliseconds = 24.8551348 days
        // (2^32) milliseconds = 49.7102696 days
        //        
        return getIntervalStr(milliseconds & 0xffffffffL);
    }
        
    public static String getIntervalStr(long milliseconds) {
        
        long ms = milliseconds % 1000;        
        long secs = milliseconds / 1000L;
        
        // we don't track ms, so round the seconds up if necessary
        if (ms >= 500) {
            secs++;
        }
        
        long days = secs / 86400;
        secs %= 86400;

        long hours = secs / 3600;
        secs %= 3600;

        long mins = secs / 60;
        secs %= 60;

        // String.format is not available in GWT clients
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            // display HH::MM::SS            
            sb.append(days);
            sb.append(days != 1 ? " days, " : " day, ");
            sb.append(hours);
            sb.append(":");
            if (mins < 10) {
                sb.append("0");
            }
            sb.append(mins);
            sb.append(":");
            if (secs < 10) {
                sb.append("0");
            }
            sb.append(secs);
        } else {
            // write out the hours, minutes and seconds long form
            if (hours > 0) {
                sb.append(hours);
                sb.append(hours != 1 ? " hours, " : " hour, ");
            }
            sb.append(mins);
            sb.append(mins != 1 ? " minutes, " : " minute, ");
            sb.append(secs);
            sb.append(secs != 1 ? " seconds" : " second");
        }
        
        return sb.toString();
    }

    public static String coordinateValue(Object x, Object y, boolean isDegrees) {
        StringBuilder sb = new StringBuilder("(");
        
        if (x != null) {
            sb.append(x);
            if (isDegrees) {
                sb.append('°');
            }
        } else {
            sb.append('-');
        }
        
        sb.append(", ");
        
        if (y != null) {
            sb.append(y);
            if (isDegrees) {
                sb.append('°');
            }
        } else {
            sb.append('-');
        }
        
        sb.append(')');
        
        return sb.toString();
    }

    public static String widthHeightValue(Integer width, Integer height) {
        String widthTxt = (width != null) ? width.toString() : "-";
        String heightTxt = (height != null) ? height.toString() : "-";
        return widthTxt + " x " + heightTxt;
    }    
    
    public static String valueWithConfidence(String value, String confidence) {
        String labelTxt = value;
        if (confidence == null) {
            confidence = "[no data]";
        }
        labelTxt += " (" + confidence + "%)";

        return labelTxt;
    }
    
    public static String accuracyValue(Integer accuracy) {
        String quantity = "[no data]";
        String units = " meters";
        if (accuracy != null) {
            if (accuracy > 1000 && (accuracy % 1000 == 0)) {
                accuracy /= 1000;
                units = " kilometers";
            }
            quantity = accuracy.toString();
        }
        return quantity + units;
    }

}
