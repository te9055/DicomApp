package com.sample;

import com.sample.model.DicomType;

import java.util.ArrayList;
import java.util.List;


public class DicomService {

    public List getAvailableBrands(DicomType type){

        List brands = new ArrayList();

        if(type.equals(DicomType.WINE)){
            brands.add("Adrianna Vineyard");
            brands.add(("J. P. Chenet"));

        }else if(type.equals(DicomType.WHISKY)){
            brands.add("Glenfiddich");
            brands.add("Johnnie Walker");

        }else if(type.equals(DicomType.BEER)){
            brands.add("Corona");

        }else {
            brands.add("No Brand Available");
        }
        return brands;
    }
}
