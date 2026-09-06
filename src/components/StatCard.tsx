import React from 'react';
import { LucideIcon } from 'lucide-react';

interface StatCardProps {
  title: string;
  value: string | number;
  subtitle?: string;
  icon: LucideIcon;
  gradient: string;
  badge?: string;
  iconBg: string;
}

export const StatCard: React.FC<StatCardProps> = ({ title, value, subtitle, icon: Icon, gradient, badge, iconBg }) => {
  return (
    <div className={`p-5 rounded-2xl bg-white border border-slate-200 shadow-sm relative overflow-hidden group hover:shadow-md hover:-translate-y-0.5 transition-all duration-200`}>
      <div className="flex items-center justify-between">
        <div>
          <p className="text-xs font-bold text-slate-500 uppercase tracking-wider">{title}</p>
          <h3 className="text-2xl font-extrabold text-slate-900 mt-1">{value}</h3>
          {subtitle && <p className="text-xs text-slate-500 mt-1 font-medium">{subtitle}</p>}
        </div>
        <div className={`p-3.5 rounded-2xl ${iconBg} shadow-md`}>
          <Icon className="w-6 h-6 text-white" />
        </div>
      </div>
      {badge && (
        <span className="mt-3 inline-block text-[11px] font-bold px-2.5 py-0.5 rounded-full bg-slate-100 text-slate-700 border border-slate-200">
          {badge}
        </span>
      )}
    </div>
  );
};
