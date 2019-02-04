package com.wunder.pets.web

import javax.inject.Inject

import com.wunder.pets.web.filters.AuthFilter
import play.api.http.DefaultHttpFilters
import play.filters.gzip.GzipFilter
import play.filters.cors.CORSFilter

class Filters @Inject() (
  cors: CORSFilter,
  auth: AuthFilter,
  gzip: GzipFilter
) extends DefaultHttpFilters(cors, auth, gzip)
