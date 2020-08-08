package com.bullhead.nafees.api.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video implements Serializable {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private String channelTitle;
    private String duration;
    private String publishedAt;
    private String channelId;

}
