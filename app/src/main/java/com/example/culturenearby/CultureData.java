package com.example.culturenearby;

public class CultureData {
    String imageUrl;
    String name;
    String info;
    String address;
    String mapLink;

    CultureData(String imageUrl,
                String name,
                String info,
                String address,
                String mapLink) {

        this.imageUrl = imageUrl;
        this.name = name;
        this.info = info;
        this.address = address;
        this.mapLink = mapLink;
    }
}
