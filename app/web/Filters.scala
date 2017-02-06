package com.wunder.pets.web

import javax.inject.Inject

import com.wunder.pets.web.filters.AuthFilter
import play.api.http.DefaultHttpFilters
import play.filters.gzip.GzipFilter

class Filters @Inject() (
  auth: AuthFilter,
  gzip: GzipFilter
) extends DefaultHttpFilters(auth, gzip)
