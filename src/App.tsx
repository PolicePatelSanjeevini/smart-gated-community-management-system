import React, { useState, useEffect } from 'react';
import { Navbar } from './components/Navbar';
import { Sidebar } from './components/Sidebar';
import { DashboardView } from './views/DashboardView';
import { ResidentsView } from './views/ResidentsView';
import { VisitorsView } from './views/VisitorsView';
import { MaintenanceView } from './views/MaintenanceView';
import { NoticesView } from './views/NoticesView';
import { ComplaintsPaymentsView } from './views/ComplaintsPaymentsView';
import {
  fetchBuildings,
  fetchFlats,
  fetchResidents,
  fetchVisitors,
  fetchMaintenance,
  fetchNotices,
  fetchComplaints,
  fetchPayments,
} from './api';
import { Building, Flat, Resident, Visitor, MaintenanceRequest, Notice, Complaint, Payment, RoleType } from './types';

export function App() {
  const [activeView, setActiveView] = useState<string>('dashboard');
  const [currentRole, setCurrentRole] = useState<RoleType>('ROLE_ADMIN');

  const [buildings, setBuildings] = useState<Building[]>([]);
  const [flats, setFlats] = useState<Flat[]>([]);
  const [residents, setResidents] = useState<Resident[]>([]);
  const [visitors, setVisitors] = useState<Visitor[]>([]);
  const [maintenance, setMaintenance] = useState<MaintenanceRequest[]>([]);
  const [notices, setNotices] = useState<Notice[]>([]);
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [payments, setPayments] = useState<Payment[]>([]);

  const loadAllData = async () => {
    const [b, f, r, v, m, n, c, p] = await Promise.all([
      fetchBuildings(),
      fetchFlats(),
      fetchResidents(),
      fetchVisitors(),
      fetchMaintenance(),
      fetchNotices(),
      fetchComplaints(),
      fetchPayments(),
    ]);

    setBuildings(b);
    setFlats(f);
    setResidents(r);
    setVisitors(v);
    setMaintenance(m);
    setNotices(n);
    setComplaints(c);
    setPayments(p);
  };

  useEffect(() => {
    loadAllData();
  }, []);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col antialiased">
      {/* Top Navbar */}
      <Navbar currentRole={currentRole} onRoleChange={setCurrentRole} activeView={activeView} />

      {/* Main Layout Container */}
      <div className="flex flex-1">
        {/* Left Sidebar */}
        <Sidebar
          activeView={activeView}
          onViewChange={setActiveView}
          counts={{
            residents: residents.length,
            visitors: visitors.length,
            maintenance: maintenance.length,
            notices: notices.length,
            payments: payments.length,
          }}
        />

        {/* Dynamic Main View Area */}
        <main className="flex-1 p-6 lg:p-8 max-w-7xl mx-auto overflow-y-auto">
          {activeView === 'dashboard' && (
            <DashboardView
              currentRole={currentRole}
              buildings={buildings}
              flats={flats}
              residents={residents}
              visitors={visitors}
              maintenance={maintenance}
              notices={notices}
              payments={payments}
              onNavigate={setActiveView}
            />
          )}

          {activeView === 'residents' && (
            <ResidentsView buildings={buildings} flats={flats} residents={residents} />
          )}

          {activeView === 'visitors' && (
            <VisitorsView visitors={visitors} residents={residents} flats={flats} onRefresh={loadAllData} />
          )}

          {activeView === 'maintenance' && (
            <MaintenanceView maintenance={maintenance} residents={residents} flats={flats} onRefresh={loadAllData} />
          )}

          {activeView === 'notices' && <NoticesView notices={notices} />}

          {activeView === 'payments' && (
            <ComplaintsPaymentsView complaints={complaints} payments={payments} onRefresh={loadAllData} />
          )}
        </main>
      </div>
    </div>
  );
}

export default App;
