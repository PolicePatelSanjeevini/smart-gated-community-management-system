import React from 'react';
import { Users, Home, UserCheck, Wrench, Megaphone, DollarSign, ArrowUpRight, ShieldCheck, MapPin } from 'lucide-react';
import { StatCard } from '../components/StatCard';
import { Building, Flat, Resident, Visitor, MaintenanceRequest, Notice, Payment, RoleType } from '../types';

interface DashboardViewProps {
  currentRole: RoleType;
  buildings: Building[];
  flats: Flat[];
  residents: Resident[];
  visitors: Visitor[];
  maintenance: MaintenanceRequest[];
  notices: Notice[];
  payments: Payment[];
  onNavigate: (view: string) => void;
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  currentRole,
  flats,
  residents,
  visitors,
  maintenance,
  notices,
  payments,
  onNavigate,
}) => {
  const occupiedFlats = flats.filter((f) => f.status === 'OCCUPIED').length;
  const vacantFlats = flats.filter((f) => f.status === 'VACANT').length;
  const insideVisitors = visitors.filter((v) => v.status === 'INSIDE').length;
  const pendingMaintenance = maintenance.filter((m) => m.status === 'PENDING' || m.status === 'IN_PROGRESS').length;
  const totalRevenue = payments.filter((p) => p.paymentStatus === 'PAID').reduce((acc, curr) => acc + curr.amount, 0);

  return (
    <div className="space-y-6">
      {/* Welcome Banner - Vibrant Colorful Hero */}
      <div className="p-6 rounded-2xl bg-gradient-to-r from-blue-700 via-indigo-700 to-purple-800 text-white shadow-xl shadow-blue-500/10 flex items-center justify-between">
        <div>
          <span className="text-xs font-extrabold px-3 py-1 rounded-full bg-white/20 text-white border border-white/30 uppercase tracking-wider">
            {currentRole === 'ROLE_ADMIN'
              ? 'Administrator Portal'
              : currentRole === 'ROLE_RESIDENT'
              ? 'Resident Self-Service'
              : currentRole === 'ROLE_SECURITY_GUARD'
              ? 'Gate Security Counter'
              : 'Maintenance Staff Operations'}
          </span>
          <h2 className="text-2xl font-extrabold text-white mt-2">Aparna Cyber Heights Control Center</h2>
          <p className="text-blue-100 text-sm mt-1 flex items-center gap-1.5 font-medium">
            <MapPin className="w-4 h-4 text-amber-300" /> Gachibowli, Hyderabad — Real-Time Community Management
          </p>
        </div>
        <div className="hidden sm:flex gap-3">
          <button
            onClick={() => onNavigate('visitors')}
            className="px-4 py-2.5 rounded-xl bg-emerald-500 hover:bg-emerald-400 text-white text-xs font-extrabold transition flex items-center gap-1.5 shadow-lg shadow-emerald-900/30"
          >
            <UserCheck className="w-4 h-4" /> Pre-Register Visitor
          </button>
          <button
            onClick={() => onNavigate('maintenance')}
            className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-400 text-white text-xs font-extrabold transition flex items-center gap-1.5 shadow-lg shadow-amber-900/30"
          >
            <Wrench className="w-4 h-4" /> Raise Maintenance
          </button>
        </div>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Total Residents"
          value={residents.length}
          subtitle={`${occupiedFlats} Occupied / ${vacantFlats} Vacant`}
          icon={Users}
          gradient=""
          iconBg="bg-blue-600"
          badge="Live Directory"
        />
        <StatCard
          title="Inside Visitors"
          value={insideVisitors}
          subtitle={`${visitors.filter((v) => v.status === 'PRE_REGISTERED').length} Expected Today`}
          icon={UserCheck}
          gradient=""
          iconBg="bg-emerald-600"
          badge="Gate Security"
        />
        <StatCard
          title="Pending Work Orders"
          value={pendingMaintenance}
          subtitle={`${maintenance.filter((m) => m.priority === 'HIGH' || m.priority === 'URGENT').length} High Priority`}
          icon={Wrench}
          gradient=""
          iconBg="bg-amber-500"
          badge="Maintenance Staff"
        />
        <StatCard
          title="Maintenance Revenue"
          value={`₹${totalRevenue.toLocaleString()}`}
          subtitle={`${payments.filter((p) => p.paymentStatus === 'PENDING').length} Pending Invoices`}
          icon={DollarSign}
          gradient=""
          iconBg="bg-purple-600"
          badge="Finance"
        />
      </div>

      {/* Quick Activity Sections */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Visitors Widget */}
        <div className="p-5 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="font-bold text-slate-900 text-base flex items-center gap-2">
              <UserCheck className="w-5 h-5 text-emerald-600" /> Recent Gate Entries
            </h3>
            <button
              onClick={() => onNavigate('visitors')}
              className="text-xs font-bold text-emerald-600 hover:text-emerald-700 flex items-center gap-1 bg-emerald-50 px-3 py-1 rounded-lg border border-emerald-200"
            >
              View All <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>
          <div className="space-y-2.5">
            {visitors.slice(0, 3).map((v) => (
              <div key={v.id} className="p-3 rounded-xl bg-slate-50 border border-slate-200 flex items-center justify-between">
                <div>
                  <p className="font-bold text-sm text-slate-900">{v.name}</p>
                  <p className="text-xs text-slate-500">
                    Flat {v.flatNumber} ({v.buildingName}) • {v.purpose}
                  </p>
                </div>
                <span
                  className={`text-[11px] font-bold px-2.5 py-1 rounded-full ${
                    v.status === 'INSIDE'
                      ? 'bg-emerald-100 text-emerald-800 border border-emerald-200'
                      : v.status === 'PRE_REGISTERED'
                      ? 'bg-blue-100 text-blue-800 border border-blue-200'
                      : 'bg-slate-100 text-slate-600'
                  }`}
                >
                  {v.status}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Latest Announcements Widget */}
        <div className="p-5 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="font-bold text-slate-900 text-base flex items-center gap-2">
              <Megaphone className="w-5 h-5 text-purple-600" /> Community Circulars & Notices
            </h3>
            <button
              onClick={() => onNavigate('notices')}
              className="text-xs font-bold text-purple-600 hover:text-purple-700 flex items-center gap-1 bg-purple-50 px-3 py-1 rounded-lg border border-purple-200"
            >
              View Board <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>
          <div className="space-y-2.5">
            {notices.slice(0, 3).map((n) => (
              <div key={n.id} className="p-3 rounded-xl bg-slate-50 border border-slate-200 space-y-1">
                <div className="flex items-center justify-between">
                  <span className="text-[10px] font-extrabold uppercase tracking-wider px-2 py-0.5 rounded bg-purple-100 text-purple-800">
                    {n.category}
                  </span>
                  <span className="text-[11px] text-slate-500 font-medium">{new Date(n.publishDate).toLocaleDateString()}</span>
                </div>
                <h4 className="font-bold text-sm text-slate-900">{n.title}</h4>
                <p className="text-xs text-slate-600 line-clamp-1">{n.content}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
