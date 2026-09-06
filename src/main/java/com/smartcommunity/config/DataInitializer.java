package com.smartcommunity.config;

import com.smartcommunity.entity.*;
import com.smartcommunity.enums.*;
import com.smartcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final FlatRepository flatRepository;
    private final ResidentRepository residentRepository;
    private final VisitorRepository visitorRepository;
    private final MaintenanceRequestRepository maintenanceRepository;
    private final ComplaintRepository complaintRepository;
    private final NoticeRepository noticeRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing Hyderabad Smart Community Seed Data...");

        // 1. Initialize Roles
        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_ADMIN).description("Admin").build()));
        Role residentRole = roleRepository.findByName(RoleType.ROLE_RESIDENT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_RESIDENT).description("Resident").build()));
        Role guardRole = roleRepository.findByName(RoleType.ROLE_SECURITY_GUARD)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_SECURITY_GUARD).description("Security Guard").build()));
        Role staffRole = roleRepository.findByName(RoleType.ROLE_MAINTENANCE_STAFF)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ROLE_MAINTENANCE_STAFF).description("Maintenance Staff").build()));

        // 2. Initialize Seed Users (Admin, Indian Residents, Guards, Specialized Staff)
        User adminUser = userRepository.findByEmail("admin@myhomebhooja.org").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("admin@myhomebhooja.org")
                        .firstName("Srinivas")
                        .lastName("Rao")
                        .phoneNumber("+91 98490 12345")
                        .password(passwordEncoder.encode("Admin@123"))
                        .active(true)
                        .roles(Set.of(adminRole))
                        .build())
        );

        User residentUser1 = userRepository.findByEmail("ananya.sharma@gmail.com").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("ananya.sharma@gmail.com")
                        .firstName("Ananya")
                        .lastName("Sharma")
                        .phoneNumber("+91 98765 43210")
                        .password(passwordEncoder.encode("Resident@123"))
                        .active(true)
                        .roles(Set.of(residentRole))
                        .build())
        );

        User residentUser2 = userRepository.findByEmail("karthik.varma@gmail.com").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("karthik.varma@gmail.com")
                        .firstName("Karthik")
                        .lastName("Varma")
                        .phoneNumber("+91 91234 56789")
                        .password(passwordEncoder.encode("Resident@123"))
                        .active(true)
                        .roles(Set.of(residentRole))
                        .build())
        );

        User residentUser3 = userRepository.findByEmail("priyanka.reddy@gmail.com").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("priyanka.reddy@gmail.com")
                        .firstName("Priyanka")
                        .lastName("Reddy")
                        .phoneNumber("+91 99887 76655")
                        .password(passwordEncoder.encode("Resident@123"))
                        .active(true)
                        .roles(Set.of(residentRole))
                        .build())
        );

        User guardUser = userRepository.findByEmail("guard.ramesh@myhomebhooja.org").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("guard.ramesh@myhomebhooja.org")
                        .firstName("Ramesh")
                        .lastName("Yadav")
                        .phoneNumber("+91 94400 11223")
                        .password(passwordEncoder.encode("Guard@123"))
                        .active(true)
                        .roles(Set.of(guardRole))
                        .build())
        );

        // Specialized Maintenance Personnel (Plumber, Gardener, Carpentry/Woodwork, Electrical)
        User plumberStaff = userRepository.findByEmail("plumber.venkatesh@myhomebhooja.org").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("plumber.venkatesh@myhomebhooja.org")
                        .firstName("Venkatesh")
                        .lastName("Rao (Plumber)")
                        .phoneNumber("+91 98480 33445")
                        .password(passwordEncoder.encode("Staff@123"))
                        .active(true)
                        .roles(Set.of(staffRole))
                        .build())
        );

        User gardenerStaff = userRepository.findByEmail("gardener.ramesh@myhomebhooja.org").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("gardener.ramesh@myhomebhooja.org")
                        .firstName("Ramesh")
                        .lastName("Kumar (Gardener)")
                        .phoneNumber("+91 98480 55667")
                        .password(passwordEncoder.encode("Staff@123"))
                        .active(true)
                        .roles(Set.of(staffRole))
                        .build())
        );

        User woodworkStaff = userRepository.findByEmail("woodwork.narsimha@myhomebhooja.org").orElseGet(() ->
                userRepository.save(User.builder()
                        .email("woodwork.narsimha@myhomebhooja.org")
                        .firstName("Narsimha")
                        .lastName("Chary (Interiors & Woodwork)")
                        .phoneNumber("+91 98480 77889")
                        .password(passwordEncoder.encode("Staff@123"))
                        .active(true)
                        .roles(Set.of(staffRole))
                        .build())
        );

        // 3. Initialize Hyderabad Towers & Flats
        if (buildingRepository.count() == 0) {
            Building b1 = buildingRepository.save(Building.builder()
                    .name("Charminar Block A")
                    .totalFloors(15)
                    .description("Premium Gachibowli High-Rise")
                    .build());

            Building b2 = buildingRepository.save(Building.builder()
                    .name("Golconda Block B")
                    .totalFloors(18)
                    .description("HITECH City View Residences")
                    .build());

            Flat f101 = flatRepository.save(Flat.builder()
                    .building(b1)
                    .flatNumber("A-301")
                    .floorNumber(3)
                    .status(FlatStatus.OCCUPIED)
                    .build());

            Flat f102 = flatRepository.save(Flat.builder()
                    .building(b1)
                    .flatNumber("A-502")
                    .floorNumber(5)
                    .status(FlatStatus.OCCUPIED)
                    .build());

            Flat f201 = flatRepository.save(Flat.builder()
                    .building(b2)
                    .flatNumber("B-704")
                    .floorNumber(7)
                    .status(FlatStatus.OCCUPIED)
                    .build());

            // 4. Initialize Resident Profiles
            Resident r1 = residentRepository.save(Resident.builder()
                    .user(residentUser1)
                    .flat(f101)
                    .residentType(ResidentType.OWNER)
                    .isPrimary(true)
                    .moveInDate(LocalDate.now().minusMonths(18))
                    .build());

            Resident r2 = residentRepository.save(Resident.builder()
                    .user(residentUser2)
                    .flat(f102)
                    .residentType(ResidentType.TENANT)
                    .isPrimary(true)
                    .moveInDate(LocalDate.now().minusMonths(8))
                    .build());

            Resident r3 = residentRepository.save(Resident.builder()
                    .user(residentUser3)
                    .flat(f201)
                    .residentType(ResidentType.OWNER)
                    .isPrimary(true)
                    .moveInDate(LocalDate.now().minusMonths(12))
                    .build());

            // 5. Seed Visitors with Indian Context
            if (visitorRepository.count() == 0) {
                visitorRepository.save(Visitor.builder()
                        .resident(r1)
                        .flat(f101)
                        .name("Raghavendra Rao")
                        .phoneNumber("+91 94405 12345")
                        .vehicleNumber("TS 07 EA 9842")
                        .purpose("Family Visit from Vijayawada")
                        .visitorType(VisitorType.GUEST)
                        .expectedArrival(Instant.now().plus(2, ChronoUnit.HOURS))
                        .status(VisitorStatus.PRE_REGISTERED)
                        .accessCode("VIS-HYD-9482")
                        .build());

                visitorRepository.save(Visitor.builder()
                        .resident(r2)
                        .flat(f102)
                        .name("Zomato Delivery - Mohammad Irfan")
                        .phoneNumber("+91 80081 22334")
                        .vehicleNumber("TS 09 F 4412")
                        .purpose("Biryani Lunch Delivery")
                        .visitorType(VisitorType.DELIVERY)
                        .expectedArrival(Instant.now().minus(30, ChronoUnit.MINUTES))
                        .entryTime(Instant.now().minus(15, ChronoUnit.MINUTES))
                        .status(VisitorStatus.INSIDE)
                        .accessCode("VIS-HYD-7714")
                        .verifiedByGuard(guardUser)
                        .build());
            }

            // 6. Seed Maintenance Requests matching Plumber, Gardener, Woodwork Staff
            if (maintenanceRepository.count() == 0) {
                maintenanceRepository.save(MaintenanceRequest.builder()
                        .resident(r1)
                        .flat(f101)
                        .assignedStaff(plumberStaff)
                        .category("Plumbing")
                        .priority(Priority.HIGH)
                        .status(RequestStatus.IN_PROGRESS)
                        .description("Balcony tap leaking water into lower floor terrace.")
                        .build());

                maintenanceRepository.save(MaintenanceRequest.builder()
                        .resident(r2)
                        .flat(f102)
                        .assignedStaff(woodworkStaff)
                        .category("Interiors & Woodwork")
                        .priority(Priority.MEDIUM)
                        .status(RequestStatus.ASSIGNED)
                        .description("Master bedroom wardrobe door hinge alignment needed.")
                        .build());

                maintenanceRepository.save(MaintenanceRequest.builder()
                        .resident(r3)
                        .flat(f201)
                        .assignedStaff(gardenerStaff)
                        .category("Gardening & Landscaping")
                        .priority(Priority.LOW)
                        .status(RequestStatus.PENDING)
                        .description("Pruning requested for private balcony potted plants.")
                        .build());
            }

            // 7. Seed Complaints
            if (complaintRepository.count() == 0) {
                complaintRepository.save(Complaint.builder()
                        .resident(r1)
                        .flat(f101)
                        .title("Visitor Parking Slot Blocked")
                        .category("Parking")
                        .status(ComplaintStatus.IN_PROGRESS)
                        .description("Unassigned commercial vehicle parked in Slot #301.")
                        .build());
            }

            // 8. Seed Hyderabad Community Notices
            if (noticeRepository.count() == 0) {
                noticeRepository.save(Notice.builder()
                        .createdByAdmin(adminUser)
                        .title("Ganesh Chaturthi Community Celebrations 2026")
                        .content("All residents are invited to the Grand Cultural Evening and Puja at the Central Clubhouse Ground on Sept 14th from 6 PM onwards.")
                        .category(NoticeCategory.EVENT)
                        .targetAudience(TargetAudience.ALL)
                        .isPinned(true)
                        .publishDate(Instant.now())
                        .build());

                noticeRepository.save(Notice.builder()
                        .createdByAdmin(adminUser)
                        .title("Manjeera Water Supply Maintenance Notice")
                        .content("HMWSSB pipeline maintenance is scheduled tomorrow between 10 AM and 2 PM. Overhead tank backup water will be supplied.")
                        .category(NoticeCategory.MAINTENANCE)
                        .targetAudience(TargetAudience.RESIDENTS_ONLY)
                        .isPinned(false)
                        .publishDate(Instant.now().minus(1, ChronoUnit.DAYS))
                        .build());
            }

            // 9. Seed Payments (INR Maintenance Dues)
            if (paymentRepository.count() == 0) {
                paymentRepository.save(Payment.builder()
                        .resident(r1)
                        .flat(f101)
                        .amount(new BigDecimal("4500.00"))
                        .feeType(FeeType.MONTHLY_MAINTENANCE)
                        .paymentStatus(PaymentStatus.PENDING)
                        .dueDate(LocalDate.now().plusDays(10))
                        .build());

                paymentRepository.save(Payment.builder()
                        .resident(r2)
                        .flat(f102)
                        .amount(new BigDecimal("4500.00"))
                        .feeType(FeeType.MONTHLY_MAINTENANCE)
                        .paymentStatus(PaymentStatus.PAID)
                        .paymentMethod(PaymentMethod.SIMULATED_UPI)
                        .transactionRef("TXN-UPI-99482019")
                        .dueDate(LocalDate.now().minusDays(5))
                        .paidAt(Instant.now().minus(2, ChronoUnit.DAYS))
                        .build());
            }
        }

        log.info("Hyderabad Smart Community Seed Data initialized successfully!");
    }
}
