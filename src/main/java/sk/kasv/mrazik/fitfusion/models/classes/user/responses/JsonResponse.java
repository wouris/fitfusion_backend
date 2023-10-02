package sk.kasv.mrazik.fitfusion.models.classes.user.responses;

import sk.kasv.mrazik.fitfusion.models.enums.ResponseType;

public record JsonResponse(ResponseType type, String message) {
}
