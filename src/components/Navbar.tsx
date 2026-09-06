import React from 'react';
import { Building2, Bell, ShieldCheck, UserCheck, Wrench, User, MapPin } from 'lucide-react';
import { RoleType } from '../types';

interface NavbarProps {
  currentRole: RoleType;
  onRoleChange: (role: RoleType) => void;
  activeView: string;
}

export const Navbar: React.FC<NavbarProps> = ({ currentRole, onRoleChange }) => {
  return (
    <header className="bg-white/90 backdrop-blur-md border-b border-slate-200 sticky top-0 z-40 px-6 py-3.5 flex items-center justify-between shadow-sm">
      {/* Brand */}
      <div className="flex items-center space-x-3">
        <div className="bg-gradient-to-tr from-blue-600 via-indigo-600 to-purple-600 p-2.5 rounded-xl shadow-md shadow-blue-500/20">
          <Building2 className="w-6 h-6 text-white" />
        </div>
        <div>
          <h1 className="font-extrabold text-lg text-slate-900 tracking-wide flex items-center gap-2">
            Aparna Cyber Heights
            <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-blue-50 text-blue-700 border border-blue-200 flex items-center gap-1">
              <MapPin className="w-3 h-3 text-blue-600" /> Gachibowli, Hyderabad
            </span>
          </h1>
          <p className="text-xs text-slate-500 font-medium">Smart Gated Community Management Platform</p>
        </div>
      </div>

      {/* Role Switcher & User Control */}
      <div className="flex items-center space-x-4">
        {/* Role Selector Tabs - Colorful Buttons */}
        <div className="bg-slate-100 p-1.5 rounded-xl border border-slate-200 hidden md:flex items-center space-x-1.5">
          <button
            onClick={() => onRoleChange('ROLE_ADMIN')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all duration-200 flex items-center space-x-1.5 ${
              currentRole === 'ROLE_ADMIN'
                ? 'bg-blue-600 text-white shadow-md shadow-blue-600/30'
                : 'text-slate-600 hover:text-blue-700 hover:bg-white'
            }`}
          >
            <ShieldCheck className="w-3.5 h-3.5" />
            <span>Admin</span>
          </button>

          <button
            onClick={() => onRoleChange('ROLE_RESIDENT')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all duration-200 flex items-center space-x-1.5 ${
              currentRole === 'ROLE_RESIDENT'
                ? 'bg-emerald-600 text-white shadow-md shadow-emerald-600/30'
                : 'text-slate-600 hover:text-emerald-700 hover:bg-white'
            }`}
          >
            <User className="w-3.5 h-3.5" />
            <span>Resident</span>
          </button>

          <button
            onClick={() => onRoleChange('ROLE_SECURITY_GUARD')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all duration-200 flex items-center space-x-1.5 ${
              currentRole === 'ROLE_SECURITY_GUARD'
                ? 'bg-amber-500 text-white shadow-md shadow-amber-500/30'
                : 'text-slate-600 hover:text-amber-700 hover:bg-white'
            }`}
          >
            <UserCheck className="w-3.5 h-3.5" />
            <span>Security Desk</span>
          </button>

          <button
            onClick={() => onRoleChange('ROLE_MAINTENANCE_STAFF')}
            className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all duration-200 flex items-center space-x-1.5 ${
              currentRole === 'ROLE_MAINTENANCE_STAFF'
                ? 'bg-purple-600 text-white shadow-md shadow-purple-600/30'
                : 'text-slate-600 hover:text-purple-700 hover:bg-white'
            }`}
          >
            <Wrench className="w-3.5 h-3.5" />
            <span>Staff</span>
          </button>
        </div>

        {/* Notifications */}
        <button className="relative p-2 rounded-xl bg-slate-100 text-slate-600 hover:text-slate-900 hover:bg-slate-200 transition border border-slate-200">
          <Bell className="w-5 h-5" />
          <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-blue-600 animate-pulse"></span>
        </button>

        {/* User Avatar Badge */}
        <div className="flex items-center space-x-3 pl-2 border-l border-slate-200">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-700 flex items-center justify-center font-bold text-white text-sm shadow-md shadow-blue-500/20">
            {currentRole === 'ROLE_ADMIN' ? 'SR' : currentRole === 'ROLE_RESIDENT' ? 'AS' : currentRole === 'ROLE_SECURITY_GUARD' ? 'RY' : 'VR'}
          </div>
          <div className="hidden lg:block text-left">
            <p className="text-xs font-bold text-slate-900">
              {currentRole === 'ROLE_ADMIN'
                ? 'Srinivas Rao (Admin)'
                : currentRole === 'ROLE_RESIDENT'
                ? 'Ananya Sharma (A-301)'
                : currentRole === 'ROLE_SECURITY_GUARD'
                ? 'Ramesh Yadav (Security)'
                : 'Venkatesh Rao (Plumber)'}
            </p>
            <p className="text-[10px] text-emerald-600 font-bold flex items-center gap-1">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span> Server Active
            </p>
          </div>
        </div>
      </div>
    </header>
  );
};
