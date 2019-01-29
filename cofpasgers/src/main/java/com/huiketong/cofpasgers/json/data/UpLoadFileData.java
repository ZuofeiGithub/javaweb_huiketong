package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.Date;

@Data
public class UpLoadFileData {
    String id;
    String filename;
    Date uploadDate;
}
