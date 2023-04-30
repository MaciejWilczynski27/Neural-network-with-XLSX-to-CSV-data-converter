package org.example;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;

public class RecordFormatter {
    public static void formatData(String sourceDirectory,String targetDirectory,String fileName,int firstIndex,int lastIndex) {
        StringBuilder stringBuilderInput;
        StringBuilder stringBuilderOutput;
        for(int i = firstIndex;i < lastIndex + 1;i++) {
            stringBuilderInput = new StringBuilder(sourceDirectory).append(fileName).append(i).append(".csv");
            stringBuilderOutput = new StringBuilder(targetDirectory).append(fileName).append(i).append("_formatted").append(".csv");
            Schema inputDataSchema = new Schema.Builder().
                    addColumnsString("Unnamed","version","tagid","success","timestamp","data__tagData__gyro__x",
                            "data__tagData__gyro__y","data__tagData__gyro__z","data__tagData__magnetic__x","data__tagData__magnetic__y",
                            "data__tagData__magnetic__z","data__tagData__quaternion__x","data__tagData__quaternion__y",
                            "data__tagData__quaternion__z","data__tagData__quaternion__w","data__tagData__linearAcceleration__x",
                            "data__tagData__linearAcceleration__y","data__tagData__linearAcceleration__z",
                            "data__tagData__pressure","data__tagData__maxLinearAcceleration","data__anchorData",
                            "data__acceleration__x","data__acceleration__y","data__acceleration__z","data__orientation__yaw",
                            "data__orientation__roll","data__orientation__pitch","data__metrics__latency","data__metrics__rates__update",
                            "data__metrics__rates__success","data__coordinates__x","data__coordinates__y","data__coordinates__z","reference__x",
                            "reference__y").build();
            TransformProcess transformProcess = new TransformProcess.Builder(inputDataSchema)
                    .removeColumns("Unnamed","version","tagid","success","timestamp","data__tagData__gyro__x",
                            "data__tagData__gyro__y","data__tagData__gyro__z","data__tagData__magnetic__x","data__tagData__magnetic__y",
                            "data__tagData__magnetic__z","data__tagData__quaternion__x","data__tagData__quaternion__y",
                            "data__tagData__quaternion__z","data__tagData__quaternion__w","data__tagData__linearAcceleration__x",
                            "data__tagData__linearAcceleration__y","data__tagData__linearAcceleration__z",
                            "data__tagData__pressure","data__tagData__maxLinearAcceleration","data__anchorData",
                            "data__acceleration__z","data__orientation__yaw",
                            "data__orientation__roll","data__orientation__pitch","data__metrics__latency","data__metrics__rates__update",
                            "data__metrics__rates__success","data__coordinates__z")
                    .convertToDouble( "data__acceleration__x")
                    .convertToDouble( "data__acceleration__y")
                    .convertToDouble("data__coordinates__x")
                    .convertToDouble("data__coordinates__y")
                    .convertToDouble("reference__x")
                    .convertToDouble("reference__y")
                    .build();
            for(int j = 0; j < transformProcess.getActionList().size();j++) {
                System.out.println("\n\n=============================");
                System.out.println("---Schema after step " + j + " (" + transformProcess.getActionList().get(j) + ") --");
                System.out.println(transformProcess.getSchemaAfterStep(j));
            }
        }
    }
}
