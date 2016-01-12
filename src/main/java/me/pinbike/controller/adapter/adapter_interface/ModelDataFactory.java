package me.pinbike.controller.adapter.adapter_interface;

import com.pinride.pinbike.config.Const;
import com.pinride.pinbike.thrift.*;
import me.pinbike.sharedjava.model.*;
import me.pinbike.sharedjava.model.base.*;
import me.pinbike.util.DateTimeUtils;
import me.pinbike.util.sample_data.SampleData;
import org.codehaus.jackson.map.ObjectMapper;
import org.fluttercode.datafactory.impl.DataFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by hpduy17 on 12/4/15.
 */
public class ModelDataFactory {
    protected DataFactory factory = new DataFactory();

    /**
     * RESPONSE OBJECT
     */
    public Bike getBike() {
        Bike bike = new Bike();
        bike.bikeId = factory.getNumberUpTo(Integer.MAX_VALUE);
        bike.description = getDescription(20);
        bike.licensePlate = factory.getNumberText(2) + "-" + factory.getRandomChars(1).toUpperCase() + factory.getNumberText(1) + " " + factory.getNumberText(4);
        bike.model = factory.getItem(SampleData.bike_models) + " " + factory.getItem(SampleData.colors);
        return bike;
    }

    public LatLng getLatLng(double lat, double lng, double radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;
        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(lat);

        LatLng latLng = new LatLng();
        latLng.lng = new_x + lng;
        latLng.lat = y + lat;

        return latLng;
    }

    public Location getLocation(double lat, double lng, double radius) {
        Location location = new Location();
        LatLng latLng = getLatLng(lat, lng, radius);
        location.lat = latLng.lat;
        location.lng = latLng.lng;
        location.address = factory.getAddress() + factory.getCity();
        return location;
    }

    public Organization getOrganization() {
        Organization organization = new Organization();
        organization.organizationId = factory.getNumberUpTo(Integer.MAX_VALUE);
        organization.organizationJoinedDate = factory.getDateBetween(new Date(1417761483000L), new Date()).getTime() / 1000;
        organization.organizationName = factory.getBusinessName();
        return organization;
    }

    public Rating getRating() {
        Rating rating = new Rating();
        rating.ratingCount = factory.getNumberUpTo(100);
        rating.totalScore = factory.getNumberBetween(0, 5);
        return rating;
    }

    public TripDetail getTripDetail() {
        TripDetail tripDetail = new TripDetail();
        tripDetail.distance = factory.getNumberBetween(1, 30);
        tripDetail.endLocation = getLocation(10.123, 106.456, 100);
        tripDetail.passengerId = factory.getNumberUpTo(Integer.MAX_VALUE);
        tripDetail.price = factory.getNumberBetween((int) (1000 * tripDetail.distance), (int) (2000 * tripDetail.distance));
        tripDetail.startLocation = getLocation(10.123, 106.456, 100);
        return tripDetail;
    }

    public UpdatedLocation getUpdatedLocation(double lat, double lng, double radius) {
        UpdatedLocation updatedLocation = new UpdatedLocation();
        updatedLocation.location = getLatLng(lat, lng, radius * 100);
        updatedLocation.updatedTime = DateTimeUtils.now();
        return updatedLocation;
    }

    public UpdatedLocation getUpdatedLocation() {
        return getUpdatedLocation(10.123, 106.456, 100);
    }

    public UpdatedStatus getUpdatedStatus() {
        UpdatedStatus updatedStatus = new UpdatedStatus();
        updatedStatus.driverId = factory.getNumberUpTo(Integer.MAX_VALUE);
        updatedStatus.tripId = factory.getNumberUpTo(Integer.MAX_VALUE);
        return updatedStatus;
    }

    public UserDetail getUserDetail() {
        UserDetail userDetail = new UserDetail();
        userDetail.avatar = factory.getItem(SampleData.avatars);
        userDetail.birthday = factory.getDateBetween(new Date(473412072000L), new Date(1135927272000L)).getTime() / 1000;
        Bike[] bikes = new Bike[]{getBike(), getBike(), getBike()};
        userDetail.bikes = Arrays.asList(bikes);
        userDetail.currentBikeId = factory.getItem(bikes).bikeId;
        userDetail.email = factory.getEmailAddress();
        userDetail.isAvailable = true;
        userDetail.joinedDate = factory.getDateBetween(new Date(1417761483000L), new Date()).getTime() / 1000;
        userDetail.name = factory.getName();
        userDetail.organizations = Arrays.asList(getOrganization(), getOrganization(), getOrganization());
        userDetail.phone = factory.getItem(SampleData.phone_prefixes) + factory.getNumberText(7);
        userDetail.rating = getRating();
        userDetail.sex = factory.getItem(SampleData.sexes);
        userDetail.status = factory.getItem(SampleData.statuses);
        userDetail.intro = getDescription(50);
        userDetail.userId = factory.getNumberUpTo(Integer.MAX_VALUE);
        return userDetail;
    }

    public UserReason getUserReason() {
        UserReason userReason = new UserReason();
        userReason.reasonType = factory.getItem(SampleData.reason_types);
        userReason.description = getDescription(20);
        return userReason;
    }

    /**
     * REQUEST OBJECT
     */

    public AcceptPassengerRequestAPI.Request getAcceptPassengerRequestAPIRequest(long driverId, long tripId) {
        AcceptPassengerRequestAPI.Request request = new AcceptPassengerRequestAPI.Request();
        request.driverId = driverId;
        request.tripId = tripId;
        return request;
    }

    public AddBikeAPI.Request getAddBikeAPIRequest(long userId) {
        AddBikeAPI.Request request = new AddBikeAPI.Request();
        request.description = getDescription(20);
        request.licensePlate = factory.getNumberText(2) + "-" + factory.getRandomChars(1).toUpperCase() + factory.getNumberText(1) + " " + factory.getNumberText(4);
        request.model = factory.getItem(SampleData.bike_models) + " " + factory.getItem(SampleData.colors);
        request.userId = userId;
        return request;
    }

    public ArrivedPickUpLocationAPI.Request getArrivedPickUpLocationAPIRequest(long driverId, long tripId) {
        ArrivedPickUpLocationAPI.Request request = new ArrivedPickUpLocationAPI.Request();
        request.driverId = driverId;
        request.tripId = tripId;
        return request;
    }

    public CancelTripAPI.Request getCancelTripAPIRequest(long userId, long tripId) {
        CancelTripAPI.Request request = new CancelTripAPI.Request();
        request.userId = userId;
        request.tripId = tripId;
        return request;
    }

    public ChangeAvailableStatusAPI.Request getChangeAvailableStatusAPIRequest(long userId, boolean isAvailable) {
        ChangeAvailableStatusAPI.Request request = new ChangeAvailableStatusAPI.Request();
        request.userId = userId;
        request.isAvailable = isAvailable;
        return request;
    }

    public CreateTripAPI.Request getCreateTripAPIRequest(long passengerId) {
        CreateTripAPI.Request request = new CreateTripAPI.Request();
        request.distance = factory.getNumberBetween(10, 50);
        request.endLocation = getLocation(10.123, 106.456, 100);
        request.passengerId = passengerId;
        request.price = factory.getNumberBetween((int) (1000 * request.distance), (int) (2000 * request.distance));
        request.startLocation = getLocation(10.123, 106.456, 100);
        return request;
    }

    public DestroyTripAPI.Request getDestroyTripAPIRequest(long tripId, long userId) {
        DestroyTripAPI.Request request = new DestroyTripAPI.Request();
        request.reason = getDescription(20);
        request.tripId = tripId;
        request.userId = userId;
        return request;
    }

    public EndTripAPI.Request getEndTripAPIRequest(long driverId, long tripId) {
        EndTripAPI.Request request = new EndTripAPI.Request();
        request.driverId = driverId;
        request.tripId = tripId;
        return request;
    }

    public GetDefaultSettingAPI.Request getDefaultSettingAPIRequest() {
        GetDefaultSettingAPI.Request request = new GetDefaultSettingAPI.Request();
        return request;
    }

    public GetDriverAroundAPI.Request getDriverAroundAPIRequest() {
        GetDriverAroundAPI.Request request = new GetDriverAroundAPI.Request();
        request.lat = 10.123;
        request.lng = 106.456;
        return request;
    }

    public GetDriverUpdatedAPI.Request getDriverUpdatedAPIRequest(long driverId, long tripId) {
        GetDriverUpdatedAPI.Request request = new GetDriverUpdatedAPI.Request();
        request.driverId = driverId;
        request.tripId = tripId;
        return request;
    }

    public GetPassengerUpdatedAPI.Request getPassengerUpdatedAPIRequest(long passengerId, long tripId) {
        GetPassengerUpdatedAPI.Request request = new GetPassengerUpdatedAPI.Request();
        request.passengerId = passengerId;
        request.tripId = tripId;
        return request;
    }

    public GetRequestFromPassengerAPI.Request getRequestFromPassengerAPIRequest(long driverId) {
        GetRequestFromPassengerAPI.Request request = new GetRequestFromPassengerAPI.Request();
        request.driverId = driverId;
        return request;
    }

    public GetUserProfileAPI.Request getUserProfileAPIRequest(long ownerId, long userId) {
        GetUserProfileAPI.Request request = new GetUserProfileAPI.Request();
        request.ownerId = ownerId;
        request.userId = userId;
        return request;
    }

    public RatingTripAPI.Request getRatingTripAPIRequest(long tripId, long userId) {
        RatingTripAPI.Request request = new RatingTripAPI.Request();
        request.tripId = tripId;
        request.userId = userId;
        request.comment = getDescription(20);
        request.score = factory.getNumberBetween(0, 5);
        return request;
    }

    public ReceivedDriverAcceptAPI.Request getReceivedDriverAcceptAPIRequest(long driverId, long tripId, long passengerId) {
        ReceivedDriverAcceptAPI.Request request = new ReceivedDriverAcceptAPI.Request();
        request.driverId = driverId;
        request.passengerId = passengerId;
        request.tripId = tripId;
        return request;
    }

    public RequestDriverAPI.Request getRequestDriverAPIRequest(long driverId, long tripId, long passengerId) {
        RequestDriverAPI.Request request = new RequestDriverAPI.Request();
        request.driverId = driverId;
        request.passengerId = passengerId;
        request.tripId = tripId;
        return request;
    }

    public StartTripAPI.Request getStartTripAPIRequest(long driverId, long tripId) {
        StartTripAPI.Request request = new StartTripAPI.Request();
        request.driverId = driverId;
        request.tripId = tripId;
        return request;
    }

    public UpdateMyCurrentBikeAPI.Request getUpdateMyCurrentBikeAPIRequest(long userId, long bikeId) {
        UpdateMyCurrentBikeAPI.Request request = new UpdateMyCurrentBikeAPI.Request();
        request.bikeId = bikeId;
        request.userId = userId;
        return request;
    }

    public UpdateMyLocationAPI.Request getUpdateMyLocationAPIRequest(long userId) {
        UpdateMyLocationAPI.Request request = new UpdateMyLocationAPI.Request();
        request.userId = userId;
        request.location = new LatLng(getLocation(10.123, 106.456, 100));
        return request;
    }

    /**
     * BACKEND OBJECT
     */

    public TBike getTBike(long userId) {
        TBike bike = new TBike();
        bike.description = getDescription(20);
        bike.licensePlate = factory.getNumberText(2) + "-" + factory.getRandomChars(1).toUpperCase() + factory.getNumberText(1) + " " + factory.getNumberText(4);
        bike.model = factory.getItem(SampleData.bike_models) + " " + factory.getItem(SampleData.colors);
        bike.userId = userId;
        return bike;
    }

    public TBroadcast getTBroadCast(long userId) {
        TBroadcast broadcast = new TBroadcast();
        broadcast.active = true;
        broadcast.deviceId = factory.getRandomChars(10);
        broadcast.regId = factory.getRandomChars(20);
        broadcast.os = Const.PinBike.BroadcastOS.IOS;
        broadcast.userId = userId;
        return broadcast;
    }

    public TConst getTConst() {
        TConst tconst = new TConst();
        tconst.aroundDistance = 5 / 100;
        tconst.coverImage = "http://www.sleekcover.com/covers/christmas-flakes-facebook-cover.jpg";
        tconst.fanPage = "https://www.facebook.com/PinBikeMe";
        tconst.notes = "";
        tconst.price = SampleData.priceModelJSON;
        tconst.qaLink = "https://www.facebook.com/PinBikeMe";
        tconst.reasonDestroyTrip = "";
        tconst.requestTimeout = 30;
        tconst.website = "http://www.pinbike.me";
        return tconst;
    }

    public TOrganization getTOrganization() {
        TOrganization organization = new TOrganization();
        organization.address = factory.getAddress() + factory.getCity();
        organization.name = factory.getBusinessName();
        return organization;
    }

    public TRating getTRating(long userId, long tripId) {
        TRating rating = new TRating();
        rating.comment = getDescription(50);
        rating.score = factory.getNumberBetween(0, 5);
        rating.tripId = tripId;
        rating.userId = userId;
        return rating;
    }

    public TTrip getTTrip(long passengerId) {
        TTrip trip = new TTrip();
        trip.datetimeEndTrip = DateTimeUtils.now() + factory.getNumberUpTo(DateTimeUtils.SECONDS_PER_DAY);
        trip.datetimeStartTrip = DateTimeUtils.now();
        trip.distance = factory.getNumberBetween(10, 50);
        Location endLocation = getLocation(10.123, 106.456, 100);
        Location startLocation = getLocation(10.123, 106.456, 100);
        TLatLng endLatLng = new TLatLng(endLocation.lat, endLocation.lng, trip.datetimeEndTrip);
        TLatLng startLatLng = new TLatLng(startLocation.lat, startLocation.lng, trip.datetimeStartTrip);
        trip.endLatLng = endLatLng;
        trip.endLocation = factory.getAddress() + factory.getCity();
        trip.estimatedTime = (long) (trip.distance * 1000 * (40 / 3600));
        trip.notes = getDescription(20);
        trip.passengerId = passengerId;
        trip.price = factory.getNumberBetween((int) (1000 * trip.distance), (int) (2000 * trip.distance));
        trip.reason = getDescription(10);
        trip.startLatLng = startLatLng;
        trip.startLocation = factory.getAddress() + factory.getCity();
        return trip;
    }

    public TUser getTUser() {
        TUser user = new TUser();
        user.avatar = factory.getItem(SampleData.avatars);
        user.brithday = factory.getDateBetween(new Date(473412072000L), new Date(1135927272000L)).getTime() / 1000;
        user.address = factory.getAddress() + factory.getCity();
        user.email = factory.getEmailAddress();
        user.socialId = factory.getEmailAddress();
        user.socialType = Const.PinBike.RegisterType.REGISTER_EMAIL;
        user.availableDriver = true;
        user.name = factory.getName();
        user.phone = factory.getItem(SampleData.phone_prefixes) + factory.getNumberText(7);
        user.sex = factory.getItem(SampleData.sexes);
        user.status = 1;
        user.bikeIds = new ArrayList<>();
        user.organizationIds = new ArrayList<>();
        user.intro = getDescription(50);
        return user;
    }

    /**
     * Custom Function by Duy Huynh
     */
    public String getDescription(int length) {
        String description = "";
        for (int i = 0; i < length; i++) {
            description += factory.getRandomWord() + " ";
        }
        return description;
    }

    public <T> T jsonToObject(String src, Class<T> type) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T result = mapper.readValue(src, type);
        return result;
    }

    public DataFactory getFactory() {
        return factory;
    }

}
