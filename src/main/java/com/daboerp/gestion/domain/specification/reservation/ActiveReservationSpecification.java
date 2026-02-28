package com.daboerp.gestion.domain.specification.reservation;

import com.daboerp.gestion.domain.entity.Reservation;
import com.daboerp.gestion.domain.specification.Specification;

/**
 * Specification for active reservations (CONFIRMED or CHECKED_IN).
 */
public class ActiveReservationSpecification implements Specification<Reservation> {

  @Override
  public boolean isSatisfiedBy(Reservation reservation) {
    return reservation.isActive();
  }
}
