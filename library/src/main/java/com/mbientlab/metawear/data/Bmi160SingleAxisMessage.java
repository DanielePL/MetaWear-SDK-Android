/*
 * Copyright 2014-2015 MbientLab Inc. All rights reserved.
 *
 * IMPORTANT: Your use of this Software is limited to those specific rights granted under the terms of a software
 * license agreement between the user who downloaded the software, his/her employer (which must be your
 * employer) and MbientLab Inc, (the "License").  You may not use this Software unless you agree to abide by the
 * terms of the License which can be found at www.mbientlab.com/terms.  The License limits your use, and you
 * acknowledge, that the Software may be modified, copied, and distributed when used in conjunction with an
 * MbientLab Inc, product.  Other than for the foregoing purpose, you may not use, reproduce, copy, prepare
 * derivative works of, modify, distribute, perform, display or sell this Software and/or its documentation for any
 * purpose.
 *
 * YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT WARRANTY
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL MBIENTLAB OR ITS LICENSORS BE LIABLE OR
 * OBLIGATED UNDER CONTRACT, NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER LEGAL EQUITABLE
 * THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT,
 * PUNITIVE OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY,
 * SERVICES, OR ANY CLAIMS BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.
 *
 * Should you have any questions regarding your right to use this Software, contact MbientLab via email:
 * hello@mbientlab.com.
 */

package com.mbientlab.metawear.data;

import java.util.Calendar;

/**
 * Container class for accelerometer data of one axis from the BMI160 chip.  The data, by default, is
 * interpreted as a float and corresponds to how much force the board is experiencing along the axis,
 * in G's.  If you interpret the data as a short or integer, the data will be converted to milliG's.
 * @author Eric Tsai
 */
public class Bmi160SingleAxisMessage extends SignedMessage {
    private final float scale;

    public Bmi160SingleAxisMessage(byte[] data, float scale) {
        this(null, data, scale);
    }

    public Bmi160SingleAxisMessage(Calendar timestamp, byte[] data, float scale) {
        super(timestamp, data);

        this.scale= scale;
    }

    /**
     * Retrieves the LSB per g ratio
     * @return Bit value corresponding to 1 g
     */
    public float getScale() {
        return scale;
    }

    @Override
    public <T> T getData(Class<T> type) {
        if (type.equals(Float.class)) {
            return type.cast(super.getData(Integer.class) / scale);
        }

        if (type.equals(Integer.class)) {
            return type.cast((int) (super.getData(Integer.class) * 1000 / scale));
        }

        if (type.equals(Short.class)) {
            return type.cast((short) (super.getData(Short.class) * 1000 / scale));
        }

        throw new UnsupportedOperationException(String.format("Type \'%s\' not supported for message class: %s",
                type.toString(), getClass().toString()));
    }

    @Override
    public String toString() {
        return String.format("{%s, scale: %.3f}", super.toString(), scale);
    }
}