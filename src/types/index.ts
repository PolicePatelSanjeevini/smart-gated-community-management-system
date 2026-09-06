export type RoleType = 'ROLE_ADMIN' | 'ROLE_RESIDENT' | 'ROLE_SECURITY_GUARD' | 'ROLE_MAINTENANCE_STAFF';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  roles: string[];
}

export interface Building {
  id: number;
  name: string;
  totalFloors: number;
  description: string;
  totalFlats: number;
  occupiedFlats: number;
}

export interface Flat {
  id: number;
  buildingId: number;
  buildingName: string;
  flatNumber: string;
  floorNumber: number;
  status: 'VACANT' | 'OCCUPIED' | 'UNDER_MAINTENANCE';
  residentCount: number;
}

export interface Resident {
  id: number;
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  flatId: number;
  flatNumber: string;
  buildingName: string;
  residentType: 'OWNER' | 'TENANT';
  isPrimary: boolean;
  moveInDate: string;
}

export interface Visitor {
  id: number;
  residentId: number;
  residentName: string;
  residentPhone: string;
  flatId: number;
  flatNumber: string;
  buildingName: string;
  name: string;
  phoneNumber: string;
  vehicleNumber?: string;
  purpose: string;
  visitorType: 'GUEST' | 'DELIVERY' | 'SERVICE_PROVIDER' | 'CAB' | 'OTHER';
  expectedArrival: string;
  entryTime?: string;
  exitTime?: string;
  status: 'PRE_REGISTERED' | 'APPROVED' | 'INSIDE' | 'CHECKED_OUT' | 'DENIED' | 'CANCELLED';
  accessCode: string;
  verifiedByGuardName?: string;
}

export interface MaintenanceRequest {
  id: number;
  residentId: number;
  residentName: string;
  flatId: number;
  flatNumber: string;
  buildingName: string;
  assignedStaffId?: number;
  assignedStaffName?: string;
  category: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  status: 'PENDING' | 'ASSIGNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  description: string;
  imageUrl?: string;
  completionNotes?: string;
  createdAt: string;
  resolvedAt?: string;
}

export interface Notice {
  id: number;
  createdByAdminId: number;
  adminName: string;
  title: string;
  content: string;
  category: 'GENERAL' | 'MAINTENANCE' | 'SECURITY' | 'EVENT' | 'EMERGENCY';
  targetAudience: 'ALL' | 'RESIDENTS_ONLY' | 'STAFF_ONLY' | 'GUARDS_ONLY';
  isPinned: boolean;
  publishDate: string;
  createdAt: string;
}

export interface Complaint {
  id: number;
  residentId: number;
  residentName: string;
  flatId: number;
  flatNumber: string;
  buildingName: string;
  assignedStaffId?: number;
  assignedStaffName?: string;
  title: string;
  category: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'REJECTED';
  description: string;
  resolutionNotes?: string;
  createdAt: string;
  resolvedAt?: string;
}

export interface Payment {
  id: number;
  residentId: number;
  residentName: string;
  flatId: number;
  flatNumber: string;
  buildingName: string;
  amount: number;
  feeType: 'MONTHLY_MAINTENANCE' | 'PARKING_FEE' | 'CLUBHOUSE_FEE' | 'SPECIAL_ASSESSMENT' | 'OTHER';
  paymentStatus: 'PENDING' | 'PAID' | 'OVERDUE' | 'FAILED';
  paymentMethod?: 'SIMULATED_CARD' | 'SIMULATED_UPI' | 'SIMULATED_NETBANKING';
  transactionRef?: string;
  dueDate: string;
  paidAt?: string;
  createdAt: string;
}

export interface AdminDashboardMetrics {
  totalResidents: number;
  totalFlats: number;
  occupiedFlats: number;
  vacantFlats: number;
  todayVisitors: number;
  pendingMaintenance: number;
  pendingComplaints: number;
  totalRevenueCollected: number;
}
