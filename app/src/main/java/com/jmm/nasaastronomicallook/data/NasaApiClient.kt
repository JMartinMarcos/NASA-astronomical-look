package com.jmm.nasaastronomicallook.data

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.funktionale.either.Either
import org.threeten.bp.LocalDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val Authorization = "Authorization"

class NasaApiClient constructor(
  baseUrl: String,
  headerUat: String,
  headerLive: String,
  userAgent: String,
  private val defaultLanguage: String
) {

  private val RUSSIAN_BASE_URL = "https://rudata.radissonhotels.com/"

  private var service: NasaService
  private val gson = Gson()

  init {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(httpLoggingInterceptor)
    clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
    clientBuilder.readTimeout(30, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(30, TimeUnit.SECONDS)

    clientBuilder.addInterceptor { chain ->
      val originalRequest = chain.request()
      val requestWithUserAgent = originalRequest.newBuilder()
        .header("User-Agent", userAgent)
        .header(headerUat, "NewWeb2019")
        .header(headerLive, "NewWeb2019")
        .build()
      chain.proceed(requestWithUserAgent)
    }

    clientBuilder.addInterceptor { chain ->
      synchronized(chain) {
        var mainResponse = chain.proceed(chain.request())
        val mainRequest = chain.request()
        if (mainResponse.code == 403) {
          val userPreferences = preferencesHelper?.getUserPreferences()
          if (userPreferences != null) {
            val response = loyaltyAuthorization(RewardLoginRequest(userPreferences.user, userPreferences.password))
            if (response.isRight()) {
              val newToken = response.right().get()
              preferencesHelper?.saveUserPreferences(UserPreferences(userPreferences.user, userPreferences.password, newToken))
              val builder = mainRequest.newBuilder()
                .header(Authorization, newToken)
                .method(mainRequest.method, mainRequest.body)
              mainResponse = chain.proceed(builder.build())
            }
          }
        }
        mainResponse
      }
    }

    val typeAdapter = object : TypeAdapter<LocalDate>() {
      override fun write(out: JsonWriter?, value: LocalDate?) {
        out?.value(value.toString())
      }

      override fun read(`in`: JsonReader?): LocalDate =
        LocalDate.parse(`in`?.nextString())
    }

    val retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(clientBuilder.build())
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    service = retrofit.create(NasaService::class.java)

  }

  private fun getLanguage(): String {
    return if (!preferencesHelper?.getSettingsPreferences()?.languageCode.isNullOrBlank()) {
      (preferencesHelper?.getSettingsPreferences()?.languageCode as String).toLowerCase()
    } else {
      defaultLanguage
    }
  }

  fun getRates(body: RatesRequest) = callService {
    service.searchRates(getLanguage(), body)
  }

  fun getHotel(hotelCode: String) = callService {
    service.searchHotel(getLanguage(), hotelCode)
  }

  fun getHotels(hotelCodes: List<String>) = callService {
    service.getHotels(getLanguage(), hotelCodes)
  }

  fun getLabels(lastUpdate: Long) = callService {
    service.getLabels(getLanguage(), lastUpdate)
  }

  fun getRooms(hotelCode: String) = callService {
    service.searchRooms(getLanguage(), hotelCode)
  }

  fun searchPreBooking(preBookingId: String) = callService {
    service.searchPreBooking(getLanguage(), preBookingId)
  }

  fun createPreBooking(request: CreatePreBookingRequest) = callService {
    service.createPreBooking(getLanguage(), request)
  }

  fun getBooking(bookingId: String) = callService {
    service.getBooking(getLanguage(), bookingId)
  }

  fun confirmBooking(preBookingId: String, request: ConfirmBookingRequest) = callService {
    service.confirmBooking(getLanguage(), preBookingId, request)
  }

  fun getCountries() = callService {
    service.getCountries(getLanguage())
  }

  fun getOfferFilterCountry() = callService {
    service.getOfferFilterCountry(getLanguage())
  }

  fun getCities(countryCode: String) = callService {
    service.getCities(getLanguage(), countryCode)
  }

  fun getCurrencies() = callService {
    service.getCurrencies(getLanguage())
  }

  fun getLanguages() = callService {
    service.getLanguages(getLanguage())
  }

  fun getHotelExtra(hotelCode: String, language: String? = null) = callService {
    service.getHotelOverview(language ?: getLanguage(), hotelCode)
  }

  fun getRateSearchTypes() = callService {
    service.getRateSearchTypes(getLanguage())
  }

  fun getHotelContacts(hotelCode: String, language: String? = null) = callService {
    service.getHotelContact(language ?: getLanguage(), hotelCode)
  }

  fun getReviewsSummary(hotelCode: String, language: String? = null) = callService {
    service.getReviewsSummary(language ?: getLanguage(), hotelCode)
  }

  fun getRestaurantReviewsSummary(restaurantCode: String) = callService {
    service.getRestaurantReviewsSummary(getLanguage(), restaurantCode)
  }

  fun getReviewsExtra(hotelCode: String, language: String? = null) = callService {
    service.getReviewsExtra(language ?: getLanguage(), hotelCode)
  }

  fun getReviews(hotelCode: String, language: String, sortBy: RatingSortEntity, offset: Int?, limit: Int?) = callService {
    service.getReviews(language, hotelCode, offset, limit, sortBy)
  }

  fun getServicesExtra(hotelCode: String, language: String? = null) = callService {
    service.getServicesExtra(language ?: getLanguage(), hotelCode)
  }

  fun getMapExtras(hotelCode: String, language: String? = null) = callService {
    service.getMapExtras(language ?: getLanguage(), hotelCode)
  }

  fun getZimbaAutocomplete(text: String) = callService {
    service.getZimbaAutocomplete(getLanguage(), text)
  }

  fun getHotelsByCity(cityCode: String, nearBy: Boolean) = callService {
    service.getHotelsByCity(getLanguage(), cityCode, nearBy)
  }

  fun getHotelsDowntownDistance(hotels: List<String>) = callService {
    service.getHotelsDowntownDistance(getLanguage(), hotels)
  }

  fun getHotelsRatings(hotels: List<String>) = callService {
    service.getHotelsRating(getLanguage(), hotels)
  }

  fun getHotelsMinimumPrices(hotelsMinPricesPreferences: HotelsMinPricesPreferences) = callService {
    service.getHotelsMinimumPrices(getLanguage(),
      hotelsMinPricesPreferences.checkInDate,
      hotelsMinPricesPreferences.checkOutDate,
      hotelsMinPricesPreferences.adults,
      hotelsMinPricesPreferences.children,
      hotelsMinPricesPreferences.searchType,
      hotelsMinPricesPreferences.rooms,
      hotelsMinPricesPreferences.promotionCode,
      hotelsMinPricesPreferences.hotelCodes,
      hotelsMinPricesPreferences.excludeMemberRates)
  }

  fun getOffersMinimumPrices(offersMinPricesRequest: OffersMinPricesRequest) = callService {
    service.getOffersMinimumPrices(getLanguage(),
      offersMinPricesRequest.hotelCodes,
      offersMinPricesRequest.startDate,
      offersMinPricesRequest.endDate,
      offersMinPricesRequest.adults,
      offersMinPricesRequest.children,
      offersMinPricesRequest.rooms,
      offersMinPricesRequest.searchType,
      offersMinPricesRequest.promotionCode,
      offersMinPricesRequest.futureDays
    )
  }

  fun getRateFlag() = callService {
    service.getRateFlag(getLanguage())
  }

  fun getAttractionsExtra(hotelCode: String, language: String? = null) = callService {
    service.getAttractionsExtra(language ?: getLanguage(), hotelCode)
  }

  fun getPois(hotelCode: String, limit: Int?) = callService {
    service.getPois(getLanguage(), hotelCode, limit)
  }

  fun getPoiTypes(hotelCode: String) = callService {
    service.getPoiTypes(getLanguage(), hotelCode)
  }

  fun getHome() = callService {
    service.getHome(getLanguage())
  }

  fun getCardTypes(cardTypeCodes: String) = callService {
    service.getCardTypes(getLanguage(), cardTypeCodes)
  }

  fun loyaltyAuthorization(rewardLoginRequest: RewardLoginRequest): Either<Exception, String> {
    return try {
      val response = service.loyaltyAuthentication(getLanguage(), rewardLoginRequest).execute()
      if (response.isValid())
        Either.right(response.headers().get(Authorization)!!)
      else {
        Either.left(validateResponseError(response))
      }
    } catch (e: Exception) {
      Either.left(UnknownException())
    }
  }

  fun loyaltyMe(authorization: String) = callService {
    service.loyaltyMe(getLanguage(), authorization)
  }

  fun loyaltySummaryMe(authorization: String) = callService {
    service.loyaltySummaryMe(getLanguage(), authorization)
  }

  fun getProfile(authorization: String) = callService {
    service.getProfile(getLanguage(), authorization)
  }

  fun putCustomerProfile(authorization: String, updateLoyaltyCustomerRequest: UpdateLoyaltyCustomerRequest) = callService {
    service.putProfile(getLanguage(), authorization, updateLoyaltyCustomerRequest)
  }

  fun enrollGuest(enrollGuestRequest: EnrollGuestRequest) = callService {
    service.enrollGuest(getLanguage(), enrollGuestRequest)
  }

  fun enrollWithPasswordRequest(enrollWithPasswordRequest: EnrollWithPasswordRequest): Either<Exception, String> {
    return try {
      val response = service.enrollWithPassword(getLanguage(), enrollWithPasswordRequest).execute()
      return if (response.isValid())
        Either.right(response.headers().get(Authorization)!!)
      else {
        Either.left(validateResponseError(response))
      }
    } catch (e: Exception) {
      Either.left(UnknownException())
    }
  }

  fun getPriceFrom(hotelCode: String) = callService {
    service.getPriceFrom(getLanguage(), hotelCode)
  }

  fun getRestaurantDetail(hotelCode: String, restaurantCode: String, language: String? = null) = callService {
    service.getRestaurantDetail(language ?: getLanguage(), hotelCode, restaurantCode)
  }

  fun getOffersExtra(hotelCode: String, language: String? = null) = callService {
    service.getOffersExtra(language ?: getLanguage(), hotelCode)
  }

  fun getMoreMenu() = callService {
    service.getMoreMenu(getLanguage())
  }

  fun getAboutMenu() = callService {
    service.getAboutMenu(getLanguage())
  }

  fun getBlockedDomain(domain: String) = callService {
    service.getBlockedDomain(getLanguage(), domain)
  }

  fun getHotelsByPoi(poiCode: String) = callService {
    service.getHotelsByPoi(getLanguage(), poiCode)
  }

  fun getPolicy(policySection: String) = callService {
    service.getPolicy(getLanguage(), policySection)
  }

  fun getBookings(bookingsRequest: BookingsRequest) = callService {
    service.getBookings(
      getLanguage(),
      bookingsRequest.firstName,
      bookingsRequest.lastName,
      bookingsRequest.bookingNumber
    )
  }

  fun getMyBookings(authorization: String) = callService {
    service.getMyBookings(getLanguage(), authorization)
  }

  fun getBanner() = callService {
    service.getBanner(getLanguage())
  }

  fun getRating(hotelCode: String) = callService {
    service.getRating(getLanguage(), hotelCode)
  }

  fun getHotelByLocation(latitude: Double, longitude: Double) = callService {
    service.getHotelByLocation(getLanguage(), latitude, longitude)
  }

  fun getBenefits() = callService {
    service.getBenefits(getLanguage())
  }

  fun getDefaultTab() = callService {
    service.getDefaultTab(getLanguage())
  }

  fun getMyEcerts(limit: String?, authorization: String) = callService {
    service.getMyEcerts(getLanguage(), authorization, limit)
  }

  fun searchEcert(code: String) = callService {
    service.searchEcert(getLanguage(), code)
  }

  fun validatePromotionalCode(searchType: String, code: String) = callService {
    service.validatePromotionalCode(getLanguage(), searchType, code)
  }

  fun getMembersHighlightedBenefits() = callService {
    service.getMembersHighlightedBenefits(getLanguage())
  }

  fun getAirlines(authorization: String) = callService {
    service.getAirlines(getLanguage(), authorization)
  }

  fun getExchangeRates(sourceCurrency: String) = callService {
    service.getExchangeRates(getLanguage(), sourceCurrency)
  }

  fun cancelBooking(bookingId: String, stayUni: String?) = callService {
    service.cancelBooking(getLanguage(), bookingId, stayUni)
  }

  fun modifyBooking(modifyBookingRequest: ModifyBookingRequest) = callService {
    service.modifyBooking(getLanguage(), modifyBookingRequest)
  }

  suspend fun modifyBookingDates(bookingId: String, modifyDates: ModifyBookingDatesRequest) = suspendCallService {
    service.modifyBookingDates(getLanguage(), bookingId, modifyDates)
  }

  fun getHeaderTransactions() = callService {
    service.getHeaderTransactions(getLanguage())
  }

  fun getTransactions(authorization: String, transactionRequest: TransactionRequest) = callService {
    service.getTransactions(getLanguage(), authorization, transactionRequest.type, transactionRequest.dateFrom, transactionRequest.dateTo)
  }

  fun applyEcert(preBookingId: String, promotionCode: String) = callService {
    service.applyEcert(getLanguage(), preBookingId, promotionCode)
  }

  fun getPrebookingEcerts(preBookingId: String) = callService {
    service.getPrebookingEcerts(getLanguage(), preBookingId)
  }

  fun requestPasswordReset(email: String) = callService {
    service.requestPasswordReset(getLanguage(), email)
  }

  fun activateAccount(activateAccountRequest: ActivateAccountRequest) = callService {
    service.activateAccount(getLanguage(), activateAccountRequest)
  }

  fun getContactPhones() = callService {
    service.getContactPhones(getLanguage())
  }

  fun getContactUsMenu() = callService {
    service.getContactUsMenu(getLanguage())
  }

  fun getAvailabilityDates(availableDatesRequest: AvailableDatesRequest) = callService {
    service.getAvailabilityDates(getLanguage(),
      availableDatesRequest.hotelCode,
      availableDatesRequest.checkInDate,
      availableDatesRequest.checkOutDate,
      availableDatesRequest.adults,
      availableDatesRequest.children,
      availableDatesRequest.rooms,
      availableDatesRequest.searchType,
      availableDatesRequest.promotionCode
    )
  }

  fun getFeaturedOffers() = callService {
    service.getFeaturedOffers(getLanguage())
  }

  fun getOffers(offersRequest: OffersRequest) = callService {
    service.getOffers(getLanguage(), offersRequest.hotelCode, offersRequest.offset, offersRequest.limit,
      offersRequest.maxOffers, offersRequest.priorities, offersRequest.brands, offersRequest.categories,
      offersRequest.country, offersRequest.city, offersRequest.radissonRewardsMembersOnly,
      offersRequest.bookingStartDate, offersRequest.bookingEndDate
    )
  }

  fun getOffer(offerId: Int) = callService {
    service.getOffer(getLanguage(), offerId)
  }

  fun getAppLanguages() = callService {
    service.getAppLanguages(getLanguage())
  }

  fun getSendEmailTopics() = callService {
    service.getSendEmailTopics(getLanguage())
  }

  fun registerOffer(registerOfferRequest: RegisterOfferRequest) = callService {
    service.registerOffer(getLanguage(), registerOfferRequest)
  }

  fun getBrands() = callService {
    service.getBrands(getLanguage())
  }

  fun getOfferBanner(offerId: Int) = callService {
    service.getOfferBanner(getLanguage(), offerId)
  }

  fun getOfferCategoryTypes() = callService {
    service.getOfferCategoryTypes(getLanguage())
  }

  fun getRestaurantOffers(hotelCode: String, language: String? = null) = callService {
    service.getRestaurantOffers(language ?: getLanguage(), hotelCode)
  }

  fun getSocialMenus() = callService {
    service.getSocialMenus(getLanguage())
  }

  fun getPhoneNumberByCountry(countryCode: String) = callService {
    service.getPhoneNumberByCountry(getLanguage(), countryCode)
  }

  fun getRedeemUrlOptions() = callService {
    service.getRedeemUrlOptions(getLanguage())
  }

  fun getConfirmationBanner() = callService {
    service.getConfirmationBanner(getLanguage())
  }

  fun getHighlightedBenefits() = callService {
    service.getHighlightedBenefits(getLanguage())
  }

  fun getEcertsReadMoreLink() = callService {
    service.getEcertsReadMoreLink(getLanguage())
  }

  fun requestUpdateProfile() = callService {
    service.requestUpdateProfile(getLanguage())
  }

  fun sendUserDataToRussianServer(userData: String) = callService {
    russianService.sendUserData(RUSSIAN_BASE_URL + "rucustomerdata/privacyImage.png" + "?data=" + userData, getLanguage())
  }

  fun createPassBook(passbook: PassbookRequest) = callServiceResponse {
    service.createPassBook(getLanguage(), passbook)
  }

  fun createRewardsPassBook(passbook: RewardsPassbookRequest) = callServiceResponse {
    service.createRewardsPassBook(getLanguage(), passbook)
  }

  fun getMinVersionApp() = callService {
    service.getMinVersionApp(getLanguage())
  }

  fun getGiftCards() = callService {
    service.getGiftCards(getLanguage())
  }

  fun loyaltyLogout(auth: String) = callService {
    service.loyaltyLogout(getLanguage(), auth)
  }

  fun saveGuestNewsletter(saveGuestNewsletterRequest: SaveGuestNewsletterRequest) = callService {
    service.saveGuestNewsletter(getLanguage(), saveGuestNewsletterRequest)
  }

  fun getMapsService() = callService {
    service.getMapsService(getLanguage())
  }

  fun getPoisNear(hotelCodes: List<String>) = callService {
    service.getPoisNear(getLanguage(), hotelCodes)
  }

  fun getTierRequirements() = callService {
    service.getTierRequirements(getLanguage())
  }

  private fun <T> callService(callback: () -> Call<T>): Either<Exception, T> {
    return try {
      val response = callback().execute()
      when {
        response.isValid() -> {
          if (response.body() != null) {
            Either.right(response.body()!!)
          } else {
            if (response.body() is Unit?)
              Either.right(Unit as T)
            else
              Either.left(validateResponseError(response))
          }
        }
        else -> Either.left(validateResponseError(response))
      }
    } catch (exception: Exception) {
      return Either.left(UnknownException())
    }
  }

  private suspend inline fun <reified T> suspendCallService(crossinline getCall: () -> Call<T>): Either<Exception, T> = suspendCoroutine { continuation ->
    getCall().enqueue(object : Callback<T> {
      override fun onFailure(call: Call<T>, t: Throwable) {
        continuation.resume(Either.left(UnknownException()))
      }

      override fun onResponse(call: Call<T>, response: Response<T>) {
        val body = response.body()
        continuation.resume(
          when {
            response.isValid() && body != null -> Either.right(body)
            response.isValid() && Unit is T -> Either.right(Unit as T)
            else -> Either.left(validateResponseError(response))
          }
        )
      }
    })
  }

  private fun <T> callServiceResponse(callback: () -> Call<T>): Either<Exception, Response<T>> {
    return try {
      val response = callback().execute()
      when {
        response.isValid() -> Either.right(response)
        else -> Either.left(validateResponseError(response))
      }
    } catch (exception: Exception) {
      return Either.left(UnknownException())
    }
  }

  private fun <T> Response<T>.isValid(): Boolean {
    return this.code() == 200 || this.code() == 204
  }

  private fun <T> validateResponseError(error: Response<T>): Exception {
    val errorBody = error.errorBody()
    return if (errorBody != null && errorBody.toString().isNotEmpty()) {
      val typeError = gson.fromJson(errorBody.charStream(), ErrorInfoEntity::class.java)

      tagManager.addErrorValues(ErrorAnalyticsEntity(error.code(), typeError.detail))
      tagManager.trackEvent(TealiumEventsTitle.ERROR_MESSAGE.title)

      parseProblem(typeError)
    } else GenericErrorException()
  }

  private fun parseProblem(typeError: ErrorInfoEntity): Exception {
    return when (typeError.type) {
      "problem:credit-card-date-validation-error" -> CreditCardDateException(typeError.detail)
      "problem:credit-card-number-validation-error" -> CreditCardNumberException(typeError.detail)
      "problem:loyalty-invalid-credentials" -> InvalidCredentialsException()
      "problem:loyalty-internal-server-error" -> InternalServerException()
      "problem:loyalty-member-exists" -> MemberExistsException(typeError.detail)
      "problem:restaurant-not-found" -> RestaurantNotFoundException(typeError.detail)
      "problem:loyalty-rewards-number-not-found" -> RewardsNumberNotFoundException(typeError.detail)
      "problem:ecert-not-found" -> EcertNotFoundException(typeError.detail)
      "problem:loyalty-member-not-found" -> LoyaltyMemberNotFoundException()
      "problem:loyalty-invalid-token" -> LoyaltyInvalidTokenException(typeError.detail)
      "problem:invalid-booking-agent-name-error" -> InvalidBookingAgentNameException(typeError.detail)
      "problem:insufficient-rewards-points-error" -> InsufficientRewardsPointsException(typeError.detail)
      "problem:loyalty-membership-number-required-error" -> LoyaltyMemberNumberRequiredException(typeError.detail)
      "problem:hotel-not-found" -> HotelNotFoundException(typeError.detail)
      "problem:policy-not-found" -> PolicyNotFoundException(typeError.detail)
      "problem:invalid-search-parameter" -> InvalidSearchParameterException(typeError.detail)
      "problem:poi-not-found" -> PoiNotFoundException(typeError.detail)
      "problem:banner-not-found" -> BannerNotFoundException(typeError.detail)
      "problem:menu-not-found" -> MenuNotFoundException(typeError.detail)
      "problem:cannot-cancel-booking" -> CannotCancelBookingException(typeError.detail)
      "problem:loyalty-number-validation-error" -> LoyaltyNumberValidationException(typeError.detail)
      "problem:loyalty-bad-request" -> LoyaltyBadRequestException(typeError.detail)
      "problem:generic-error" -> GenericErrorException()
      "problem:special-rate-code-expired" -> SpecialRateCodeExpiredException()
      "problem:special-rate-code-invalid" -> SpecialRateCodeInvalidException()
      "problem:labels-not-found" -> LabelsNotFoundException(typeError.detail)
      "problem:incorrect-name" -> ActivateAccountIncorrectNameException()
      "problem:incorrect-surname" -> ActivateAccountIncorrectSurNameException()
      "problem:account-already-activated" -> ActivateAccountAlreadyActivatedException()
      "problem:account-closed-or-merged" -> ActivateAccountClosedMergedException()
      "problem:incorrect-email" -> IncorrectEmailException()
      "problem:incorrect-password" -> IncorrectPasswordException()
      "problem:all-benefits-rewards-not-found" -> AllBenefitsRewardsNotFoundException(typeError.detail)
      "problem:contact-us-emails-not-found" -> ContactUsEmailsNotFoundException(typeError.detail)
      "problem:highlighted-benefits-slider-not-found" -> HighlightedBenefitsSliderNotFoundException(typeError.detail)
      "problem:home-benefits-slider-not-found" -> HomeBenefitsSliderNotFoundException(typeError.detail)
      "problem:members-highlighted-benefits-not-found" -> MembersHighlightedlBenefitsNotFoundException(typeError.detail)
      "problem:room-display-default-tab-not-found" -> RoomDisplayDefaultTabNotFoundException(typeError.detail)
      "problem:insufficient-data-offer-register" -> OfferRegistrationInsufficientDataException()
      "problem:partner-not-found" -> OfferRegistrationPartnerNotFoundException()
      "problem:duplicate-offer-registration" -> OfferRegistrationDuplicatedException()
      "problem:invalid-email-offer-register" -> OfferRegistrationInvalidEmailException()
      "problem:invalid-status-offer-register" -> OfferRegistrationInvalidStatusException()
      "problem:loyalty-account-not-activated" -> LoyaltyAccountNotActivatedException()
      "problem:loyalty-too-many-requests" -> LoyaltyTooManyRequestException()
      "problem:itinerary-not-found" -> ItineraryNotFoundException()
      "problem:rewards-my-statements-not-found" -> MyStatementsNotFoundException(typeError.detail)
      "problem:restaurant-hotel-offers-not-found" -> RestaurantHotelOffersNotFoundException(typeError.detail)
      "problem:modify-dates-not-allowed" -> ModifyDatesNotAllowedException(typeError.detail)
      "problem:rate-unavailable-for-booking" -> RateUnavailableForBookingException(typeError.detail)
      "problem:room-closed" -> RoomClosedException(typeError.detail)

      else -> GenericErrorException()
    }
  }
}
