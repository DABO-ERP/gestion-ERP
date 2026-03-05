---
name: hospitality-management-domain-expertise
description: Use this skill when making product, domain, data model, workflow, or business-rule decisions for hotel and hostel management software.
---

## Purpose of this Skill

This Agent Skill provides **deep domain context** for the hospitality industry (hotels, hostels, boutique stays, and small accommodations).  
Its goal is to ensure an AI agent can **make correct architectural, functional, and business decisions** when designing or implementing a **Hotel / Hostel Management System (HMS / PMS)**.

This is **not** a UI skill or a technical framework skill.  
It is a **business-domain intelligence layer**.

---

## Core Hospitality Concepts (Must Be Understood)

### Property Types
- Hotel (full-service, limited-service, boutique)
- Hostel (shared rooms, dorms, mixed/private)
- Guesthouse / B&B
- Aparthotel

Key implication: **room logic, pricing, and guest handling differ by property type**.

---

### Rooms, Beds, and Inventory

Hospitality inventory is **perishable and time-bound**.

#### Room Concepts
- Room Type (e.g. Standard, Deluxe, Dorm)
- Physical Room (Room 101, Room 102)
- Bed (critical for hostels)

#### Inventory Rules
- A room can be:
  - Available
  - Occupied
  - Out of Order (maintenance)
  - Blocked (owner use, group hold)

- Hostels often sell:
  - Beds within a room
  - Entire private rooms
  - Mixed availability (beds + private)

**Inventory must always be date-based.**

---

## Reservations & Booking Logic

### Reservation Lifecycle
- Inquiry
- Tentative / On-hold
- Confirmed
- Checked-in
- Checked-out
- Cancelled
- No-show

Each state change impacts:
- Inventory
- Housekeeping
- Billing
- Reporting

---

### Booking Attributes
- Check-in date
- Check-out date
- Number of guests
- Room(s) or bed(s)
- Rate plan
- Source channel

Never assume:
- One room = one guest
- One reservation = one room

---

### Group Bookings
- Multiple rooms or beds under one reservation
- Split payments
- Rooming lists
- Shared invoices

---

## Pricing & Rate Management

### Rate Types
- Standard rate
- Non-refundable
- Seasonal rates
- Weekend rates
- Long-stay discounts
- Promotional codes

Rates are usually:
- Per night
- Per room or per bed
- Per occupancy level

### Key Rule
**Pricing logic must be decoupled from room availability logic.**

---

## Guests & Profiles

### Guest vs Reservation
- A guest is a person
- A reservation is a booking container

A single guest can:
- Have multiple reservations
- Be part of group reservations

Guest profiles include:
- Identification (passport, ID)
- Contact info
- Preferences
- Stay history

Legal requirements vary by country (especially ID storage).

---

## Check-in / Check-out Operations

### Check-in
- Identity verification
- Room assignment (manual or automatic)
- Deposit handling
- Key issuance

### Check-out
- Final billing
- Extras posting
- Room status update
- Housekeeping trigger

**Late check-outs and early arrivals affect availability.**

---

## Housekeeping & Maintenance

### Housekeeping States
- Clean
- Dirty
- Inspected
- Out of Service

Housekeeping is tightly coupled to:
- Check-outs
- Room moves
- Maintenance events

### Maintenance
- Preventive
- Corrective
- Room blocking logic

A room under maintenance must be **unavailable for sale**.

---

## Billing, Payments & Accounting

### Charges
- Room charges (per night)
- Extras (minibar, laundry, tours)
- Taxes (local, city, VAT)
- Fees (cleaning, service)

### Payments
- Cash
- Credit/Debit
- Online prepayment
- Partial payments
- Deposits

Important:
- Invoices ≠ Payments
- One reservation can have multiple payments
- One payment can cover multiple reservations (groups)

---

## Channel Management (Distribution)

### Channels
- Direct booking (website, walk-in)
- OTAs (Booking.com, Hostelworld, Airbnb)
- Corporate contracts
- Travel agents

Key challenges:
- Overbooking prevention
- Rate parity
- Inventory synchronization

Channel logic must be **loosely coupled** and **event-driven**.

---

## Reporting & KPIs

Common hospitality metrics:
- Occupancy Rate
- ADR (Average Daily Rate)
- RevPAR
- Length of Stay
- Cancellation rate
- No-show rate

Reports are:
- Daily
- Monthly
- By channel
- By room type

Reporting logic must rely on **historical immutable data**, not current state.

---

## Legal & Operational Constraints

- Guest registration laws
- Tax reporting
- Data privacy (GDPR, etc.)
- Fiscal invoices (country-specific)

Never assume:
- Same tax rules
- Same guest ID rules
- Same invoice requirements

---

## Design Principles This Skill Enforces

When building an application:
- Always think in **date ranges**
- Treat inventory as **time-sliced**
- Separate:
  - Availability
  - Pricing
  - Billing
  - Operations
- Prefer **state machines** for reservations and rooms
- Avoid hardcoding hotel assumptions (hostels differ)

---

## Red Flags (Domain Bugs)

- Assuming one guest per room
- Ignoring no-shows
- Mixing payments with invoices
- Not tracking room state history
- Treating availability as a boolean instead of date-based

These are **domain-critical errors**, not minor issues.

---

## How an AI Agent Should Use This Skill

Use this skill:
- When defining data models
- When deciding workflows
- When naming entities and aggregates
- When prioritizing features
- When resolving ambiguities in requirements

If a technical decision contradicts hospitality domain rules,  
**the domain rules win**.