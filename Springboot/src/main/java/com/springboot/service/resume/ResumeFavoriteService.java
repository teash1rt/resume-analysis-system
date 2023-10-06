package com.springboot.service.resume;

import com.springboot.common.R;

public interface ResumeFavoriteService {
    R add_favorite(Integer rid);

    R cancel_favorite(Integer rid);

    R get_favorite();
}
