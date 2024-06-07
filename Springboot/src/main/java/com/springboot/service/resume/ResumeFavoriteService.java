package com.springboot.service.resume;

import com.springboot.common.R;

public interface ResumeFavoriteService {
    R favoriteResume(Integer rid, Boolean isFavorite);

    R getFavoriteResumes();
}
