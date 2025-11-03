package com.kv1k1.resource;

import com.kv1k1.dto.AdDTO;
import com.kv1k1.dto.AdMapper;
import com.kv1k1.entity.Ad;
import com.kv1k1.service.AdService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Path("/ads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdResource {

    @Inject
    AdService adService;

    @Inject
    JsonWebToken jwt;

    @Inject
    Logger logger;

    @GET
    @Path("/random")
    public Response getRandomAd() {
        try {
            var adOpt = adService.getRandomActiveAd();
            if (adOpt.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("No active ads available"))
                        .build();
            }

            Ad ad = adOpt.get();
            adService.recordAdDisplay(ad.id);

            logger.info("Displayed ad: " + ad.id + " - " + ad.imageUrl);
            return Response.ok(AdMapper.toDto(ad)).build();

        } catch (Exception e) {
            logger.error("Error getting random ad", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Record ad click
    @POST
    @Path("/{id}/click")
    public Response recordClick(@PathParam("id") Long id) {
        try {
            Ad ad = adService.getAdById(id);
            if (ad == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            adService.recordAdClick(id);
            logger.info("Ad clicked: " + id);
            return Response.ok().build();

        } catch (Exception e) {
            logger.error("Error recording ad click", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Admin endpoints
    @GET
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public List<AdDTO> getAllAdsAdmin() {
        return adService.getAllAds().stream()
                .map(AdMapper::toDto)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/admin")
    @RolesAllowed("ADMIN")
    public Response createAdAdmin(AdDTO adDTO) {
        try {
            // Set the creator from JWT
            adDTO.createdBy = jwt.getName();

            Ad ad = AdMapper.toEntity(adDTO);
            Ad created = adService.createAd(ad);

            logger.info("Admin created ad: " + created.id + " by " + jwt.getName());
            return Response.status(Response.Status.CREATED)
                    .entity(AdMapper.toDto(created))
                    .build();

        } catch (Exception e) {
            logger.error("Error creating ad", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Failed to create ad"))
                    .build();
        }
    }

    @DELETE
    @Path("/admin/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteAdAdmin(@PathParam("id") Long id) {
        boolean deleted = adService.deleteAd(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        logger.info("Admin deleted ad: " + id + " by " + jwt.getName());
        return Response.noContent().build();
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}