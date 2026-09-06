import React from 'react';
import { LayoutDashboard, Users, UserCheck, Wrench, Megaphone, CreditCard } from 'lucide-react';

interface SidebarProps {
  activeView: string;
  onViewChange: (view: string) => void;
  counts: {
    residents: number;
    visitors: number;
    maintenance: number;
    notices: number;
    payments: number;
  };
}

export const Sidebar: React.FC<SidebarProps> = ({ activeView, onViewChange, counts }) => {
  const menuItems = [
    { id: 'dashboard', label: 'Dashboard Overview', icon: LayoutDashboard, badge: null, activeBg: 'bg-blue-600 text-white shadow-md shadow-blue-600/30', color: 'text-blue-600' },
    { id: 'residents', label: 'Flats & Residents', icon: Users, badge: counts.residents, activeBg: 'bg-indigo-600 text-white shadow-md shadow-indigo-600/30', color: 'text-indigo-600' },
    { id: 'visitors', label: 'Visitor Gate Pass', icon: UserCheck, badge: counts.visitors, activeBg: 'bg-emerald-600 text-white shadow-md shadow-emerald-600/30', color: 'text-emerald-600' },
    { id: 'maintenance', label: 'Maintenance Requests', icon: Wrench, badge: counts.maintenance, activeBg: 'bg-amber-500 text-white shadow-md shadow-amber-500/30', color: 'text-amber-500' },
    { id: 'notices', label: 'Notice Board', icon: Megaphone, badge: counts.notices, activeBg: 'bg-purple-600 text-white shadow-md shadow-purple-600/30', color: 'text-purple-600' },
    { id: 'payments', label: 'Complaints & Dues', icon: CreditCard, badge: counts.payments, activeBg: 'bg-rose-600 text-white shadow-md shadow-rose-600/30', color: 'text-rose-600' },
  ];

  return (
    <aside className="w-64 bg-white border-r border-slate-200 flex flex-col justify-between h-[calc(100vh-65px)] sticky top-[65px] p-4 select-none shadow-sm">
      <div className="space-y-1.5">
        <p className="px-3 text-[11px] font-extrabold text-slate-400 uppercase tracking-wider mb-2">Community Modules</p>
        {menuItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeView === item.id;
          return (
            <button
              key={item.id}
              onClick={() => onViewChange(item.id)}
              className={`w-full flex items-center justify-between px-3.5 py-2.5 rounded-xl text-sm font-bold transition-all duration-200 ${
                isActive
                  ? item.activeBg
                  : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'
              }`}
            >
              <div className="flex items-center space-x-3">
                <Icon className={`w-5 h-5 ${isActive ? 'text-white' : item.color}`} />
                <span>{item.label}</span>
              </div>
              {item.badge !== null && (
                <span
                  className={`text-xs px-2 py-0.5 rounded-full font-extrabold ${
                    isActive ? 'bg-white/20 text-white' : 'bg-slate-100 text-slate-600 border border-slate-200'
                  }`}
                >
                  {item.badge}
                </span>
              )}
            </button>
          );
        })}
      </div>

      {/* Footer Info */}
      <div className="p-3.5 bg-slate-50 rounded-xl border border-slate-200 text-xs text-slate-600 space-y-1">
        <p className="font-bold text-slate-800">Aparna Cyber Heights</p>
        <p className="text-[11px] text-slate-500 font-medium">Gachibowli • Hyderabad</p>
      </div>
    </aside>
  );
};
