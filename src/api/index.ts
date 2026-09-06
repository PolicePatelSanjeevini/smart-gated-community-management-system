import axios from 'axios';
import { Building, Flat, Resident, Visitor, MaintenanceRequest, Notice, Complaint, Payment } from '../types';

const API_BASE = '/api';

export const api = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Hyderabad Community Seed Data
const MOCK_BUILDINGS: Building[] = [
  { id: 1, name: 'Charminar Block A', totalFloors: 15, description: 'Premium Gachibowli High-Rise', totalFlats: 30, occupiedFlats: 26 },
  { id: 2, name: 'Golconda Block B', totalFloors: 18, description: 'HITECH City View Residences', totalFlats: 36, occupiedFlats: 30 },
  { id: 3, name: 'Kakatiya Villa C', totalFloors: 5, description: 'Exclusive Duplex Penthouses', totalFlats: 10, occupiedFlats: 9 },
];

const MOCK_FLATS: Flat[] = [
  { id: 1, buildingId: 1, buildingName: 'Charminar Block A', flatNumber: 'A-301', floorNumber: 3, status: 'OCCUPIED', residentCount: 3 },
  { id: 2, buildingId: 1, buildingName: 'Charminar Block A', flatNumber: 'A-502', floorNumber: 5, status: 'OCCUPIED', residentCount: 4 },
  { id: 3, buildingId: 1, buildingName: 'Charminar Block A', flatNumber: 'A-801', floorNumber: 8, status: 'VACANT', residentCount: 0 },
  { id: 4, buildingId: 2, buildingName: 'Golconda Block B', flatNumber: 'B-704', floorNumber: 7, status: 'OCCUPIED', residentCount: 2 },
  { id: 5, buildingId: 2, buildingName: 'Golconda Block B', flatNumber: 'B-1202', floorNumber: 12, status: 'UNDER_MAINTENANCE', residentCount: 0 },
];

const MOCK_RESIDENTS: Resident[] = [
  { id: 1, userId: 2, firstName: 'Ananya', lastName: 'Sharma', email: 'ananya.sharma@gmail.com', phoneNumber: '+91 98765 43210', flatId: 1, flatNumber: 'A-301', buildingName: 'Charminar Block A', residentType: 'OWNER', isPrimary: true, moveInDate: '2025-03-15' },
  { id: 2, userId: 3, firstName: 'Karthik', lastName: 'Varma', email: 'karthik.varma@gmail.com', phoneNumber: '+91 91234 56789', flatId: 2, flatNumber: 'A-502', buildingName: 'Charminar Block A', residentType: 'TENANT', isPrimary: true, moveInDate: '2026-01-10' },
  { id: 3, userId: 4, firstName: 'Priyanka', lastName: 'Reddy', email: 'priyanka.reddy@gmail.com', phoneNumber: '+91 99887 76655', flatId: 4, flatNumber: 'B-704', buildingName: 'Golconda Block B', residentType: 'OWNER', isPrimary: true, moveInDate: '2024-11-01' },
];

const MOCK_VISITORS: Visitor[] = [
  { id: 1, residentId: 1, residentName: 'Ananya Sharma', residentPhone: '+91 98765 43210', flatId: 1, flatNumber: 'A-301', buildingName: 'Charminar Block A', name: 'Raghavendra Rao', phoneNumber: '+91 94405 12345', vehicleNumber: 'TS 07 EA 9842', purpose: 'Family Visit from Vijayawada', visitorType: 'GUEST', expectedArrival: new Date(Date.now() + 3600000 * 2).toISOString(), status: 'PRE_REGISTERED', accessCode: 'VIS-HYD-9482' },
  { id: 2, residentId: 2, residentName: 'Karthik Varma', residentPhone: '+91 91234 56789', flatId: 2, flatNumber: 'A-502', buildingName: 'Charminar Block A', name: 'Zomato Delivery - Mohammad Irfan', phoneNumber: '+91 80081 22334', vehicleNumber: 'TS 09 F 4412', purpose: 'Biryani Lunch Delivery', visitorType: 'DELIVERY', expectedArrival: new Date(Date.now() - 1800000).toISOString(), entryTime: new Date(Date.now() - 900000).toISOString(), status: 'INSIDE', accessCode: 'VIS-HYD-7714', verifiedByGuardName: 'Ramesh Yadav' },
  { id: 3, residentId: 3, residentName: 'Priyanka Reddy', residentPhone: '+91 99887 76655', flatId: 4, flatNumber: 'B-704', buildingName: 'Golconda Block B', name: 'UrbanCompany Electrician - Mahesh', phoneNumber: '+91 98480 11223', purpose: 'AC Filter Service', visitorType: 'SERVICE_PROVIDER', expectedArrival: new Date(Date.now() - 7200000).toISOString(), entryTime: new Date(Date.now() - 7000000).toISOString(), exitTime: new Date(Date.now() - 3600000).toISOString(), status: 'CHECKED_OUT', accessCode: 'VIS-HYD-1029', verifiedByGuardName: 'Ramesh Yadav' },
];

const MOCK_MAINTENANCE: MaintenanceRequest[] = [
  { id: 1, residentId: 1, residentName: 'Ananya Sharma', flatId: 1, flatNumber: 'A-301', buildingName: 'Charminar Block A', assignedStaffId: 5, assignedStaffName: 'Venkatesh Rao (Plumber)', category: 'Plumbing', priority: 'HIGH', status: 'IN_PROGRESS', description: 'Balcony tap leaking water into lower floor terrace.', createdAt: new Date(Date.now() - 86400000).toISOString() },
  { id: 2, residentId: 2, residentName: 'Karthik Varma', flatId: 2, flatNumber: 'A-502', buildingName: 'Charminar Block A', assignedStaffId: 7, assignedStaffName: 'Narsimha Chary (Interiors & Woodwork)', category: 'Interiors & Woodwork', priority: 'MEDIUM', status: 'ASSIGNED', description: 'Master bedroom wardrobe door hinge alignment needed.', createdAt: new Date(Date.now() - 43200000).toISOString() },
  { id: 3, residentId: 3, residentName: 'Priyanka Reddy', flatId: 4, flatNumber: 'B-704', buildingName: 'Golconda Block B', assignedStaffId: 6, assignedStaffName: 'Ramesh Kumar (Gardener)', category: 'Gardening & Landscaping', priority: 'LOW', status: 'PENDING', description: 'Pruning requested for private balcony potted plants.', createdAt: new Date(Date.now() - 172800000).toISOString() },
];

const MOCK_NOTICES: Notice[] = [
  { id: 1, createdByAdminId: 1, adminName: 'Srinivas Rao (Admin)', title: 'Ganesh Chaturthi Community Celebrations 2026', content: 'All residents are invited to the Grand Cultural Evening and Mahaprasadam at the Central Clubhouse Ground on Sept 14th from 6 PM onwards.', category: 'EVENT', targetAudience: 'ALL', isPinned: true, publishDate: new Date().toISOString(), createdAt: new Date().toISOString() },
  { id: 2, createdByAdminId: 1, adminName: 'Srinivas Rao (Admin)', title: 'Manjeera Water Supply Maintenance Notice', content: 'HMWSSB pipeline maintenance is scheduled tomorrow between 10 AM and 2 PM. Overhead tank backup water will be supplied.', category: 'MAINTENANCE', targetAudience: 'RESIDENTS_ONLY', isPinned: false, publishDate: new Date(Date.now() - 86400000).toISOString(), createdAt: new Date(Date.now() - 86400000).toISOString() },
  { id: 3, createdByAdminId: 1, adminName: 'Srinivas Rao (Admin)', title: 'New Gate RFID Vehicle Sticker Distribution', content: 'Security desk is issuing automated RFID stickers for Gachibowli main gate entry. Please submit vehicle RC copy at Admin Office.', category: 'SECURITY', targetAudience: 'ALL', isPinned: false, publishDate: new Date(Date.now() - 259200000).toISOString(), createdAt: new Date(Date.now() - 259200000).toISOString() },
];

const MOCK_COMPLAINTS: Complaint[] = [
  { id: 1, residentId: 1, residentName: 'Ananya Sharma', flatId: 1, flatNumber: 'A-301', buildingName: 'Charminar Block A', assignedStaffId: 5, assignedStaffName: 'Venkatesh Rao (Plumber)', title: 'Visitor Parking Slot Blocked', category: 'Parking', status: 'IN_PROGRESS', description: 'Unassigned commercial delivery van parked in reserved Slot #301.', createdAt: new Date(Date.now() - 86400000).toISOString() },
];

const MOCK_PAYMENTS: Payment[] = [
  { id: 1, residentId: 1, residentName: 'Ananya Sharma', flatId: 1, flatNumber: 'A-301', buildingName: 'Charminar Block A', amount: 4500.00, feeType: 'MONTHLY_MAINTENANCE', paymentStatus: 'PENDING', dueDate: '2026-09-20', createdAt: new Date().toISOString() },
  { id: 2, residentId: 2, residentName: 'Karthik Varma', flatId: 2, flatNumber: 'A-502', buildingName: 'Charminar Block A', amount: 4500.00, feeType: 'MONTHLY_MAINTENANCE', paymentStatus: 'PAID', paymentMethod: 'SIMULATED_UPI', transactionRef: 'TXN-UPI-99482019', dueDate: '2026-09-01', paidAt: new Date(Date.now() - 172800000).toISOString(), createdAt: new Date().toISOString() },
  { id: 3, residentId: 3, residentName: 'Priyanka Reddy', flatId: 4, flatNumber: 'B-704', buildingName: 'Golconda Block B', amount: 1500.00, feeType: 'CLUBHOUSE_FEE', paymentStatus: 'PAID', paymentMethod: 'SIMULATED_CARD', transactionRef: 'TXN-CARD-10492812', dueDate: '2026-08-15', paidAt: new Date(Date.now() - 864000000).toISOString(), createdAt: new Date().toISOString() },
];

// Helper wrapper to attempt backend call with fallback
async function fetchWithFallback<T>(apiCall: () => Promise<any>, fallbackData: T): Promise<T> {
  try {
    const res = await apiCall();
    if (res.data && res.data.success) {
      return res.data.data;
    }
    return fallbackData;
  } catch (err) {
    return fallbackData;
  }
}

export const fetchBuildings = () => fetchWithFallback(() => api.get('/buildings'), MOCK_BUILDINGS);
export const fetchFlats = () => fetchWithFallback(() => api.get('/flats'), MOCK_FLATS);
export const fetchResidents = () => fetchWithFallback(() => api.get('/residents'), MOCK_RESIDENTS);
export const fetchVisitors = () => fetchWithFallback(() => api.get('/visitors'), MOCK_VISITORS);
export const fetchMaintenance = () => fetchWithFallback(() => api.get('/maintenance'), MOCK_MAINTENANCE);
export const fetchNotices = () => fetchWithFallback(() => api.get('/notices'), MOCK_NOTICES);
export const fetchComplaints = () => fetchWithFallback(() => api.get('/complaints'), MOCK_COMPLAINTS);
export const fetchPayments = () => fetchWithFallback(() => api.get('/payments'), MOCK_PAYMENTS);

export const registerVisitorApi = async (data: any): Promise<Visitor> => {
  try {
    const res = await api.post('/visitors/pre-register', data);
    return res.data.data;
  } catch (err) {
    const newVisitor: Visitor = {
      id: Date.now(),
      residentId: data.residentId,
      residentName: 'Ananya Sharma',
      residentPhone: '+91 98765 43210',
      flatId: data.flatId,
      flatNumber: 'A-301',
      buildingName: 'Charminar Block A',
      name: data.name,
      phoneNumber: data.phoneNumber,
      vehicleNumber: data.vehicleNumber,
      purpose: data.purpose,
      visitorType: data.visitorType,
      expectedArrival: data.expectedArrival,
      status: 'PRE_REGISTERED',
      accessCode: 'VIS-HYD-' + Math.floor(1000 + Math.random() * 9000),
    };
    return newVisitor;
  }
};

export const checkInVisitorApi = async (accessCode: string): Promise<any> => {
  try {
    const res = await api.post('/visitors/check-in', { accessCode });
    return res.data.data;
  } catch (err) {
    return { accessCode, status: 'INSIDE', entryTime: new Date().toISOString() };
  }
};

export const checkOutVisitorApi = async (accessCode: string): Promise<any> => {
  try {
    const res = await api.post('/visitors/check-out', { accessCode });
    return res.data.data;
  } catch (err) {
    return { accessCode, status: 'CHECKED_OUT', exitTime: new Date().toISOString() };
  }
};

export const createMaintenanceApi = async (data: any): Promise<MaintenanceRequest> => {
  try {
    const res = await api.post('/maintenance', data);
    return res.data.data;
  } catch (err) {
    return {
      id: Date.now(),
      residentId: data.residentId,
      residentName: 'Ananya Sharma',
      flatId: data.flatId,
      flatNumber: 'A-301',
      buildingName: 'Charminar Block A',
      category: data.category,
      priority: data.priority || 'MEDIUM',
      status: 'PENDING',
      description: data.description,
      createdAt: new Date().toISOString(),
    };
  }
};

export const processPaymentSimulatedApi = async (paymentId: number, paymentMethod: string): Promise<Payment> => {
  try {
    const res = await api.post('/payments/simulate-pay', { paymentId, paymentMethod });
    return res.data.data;
  } catch (err) {
    return {
      id: paymentId,
      residentId: 1,
      residentName: 'Ananya Sharma',
      flatId: 1,
      flatNumber: 'A-301',
      buildingName: 'Charminar Block A',
      amount: 4500.00,
      feeType: 'MONTHLY_MAINTENANCE',
      paymentStatus: 'PAID',
      paymentMethod: paymentMethod as any,
      transactionRef: 'TXN-UPI-' + Math.floor(10000000 + Math.random() * 90000000),
      dueDate: '2026-09-20',
      paidAt: new Date().toISOString(),
      createdAt: new Date().toISOString(),
    };
  }
};
