package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

public class SpecialofferData {
    List<SpecialofferType> type;
    List<SpecialofferStyle> style;

    public List<SpecialofferType> getType() {
        return type;
    }

    public SpecialofferData setType(List<SpecialofferType> type) {
        this.type = type;
        return this;
    }

    public List<SpecialofferStyle> getStyle() {
        return style;
    }

    public SpecialofferData setStyle(List<SpecialofferStyle> style) {
        this.style = style;
        return this;
    }
}
