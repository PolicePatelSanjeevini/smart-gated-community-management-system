import React, { useState } from 'react';
import { Notice } from '../types';
import { Megaphone, Pin, Calendar, ShieldAlert, Wrench, Info } from 'lucide-react';

interface NoticesViewProps {
  notices: Notice[];
}

export const NoticesView: React.FC<NoticesViewProps> = ({ notices }) => {
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');

  const filteredNotices = notices.filter((n) => selectedCategory === 'ALL' || n.category === selectedCategory);

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case 'MAINTENANCE':
        return <Wrench className="w-4 h-4 text-amber-400" />;
      case 'SECURITY':
        return <ShieldAlert className="w-4 h-4 text-rose-400" />;
      default:
        return <Info className="w-4 h-4 text-sky-400" />;
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-white flex items-center gap-2">
            <Megaphone className="w-6 h-6 text-purple-400" /> Community Notice Board & Announcements
          </h2>
          <p className="text-xs text-slate-400 mt-1">Official circulars, maintenance schedules, and community event notices</p>
        </div>

        {/* Category Filters */}
        <div className="flex items-center space-x-2">
          {['ALL', 'GENERAL', 'MAINTENANCE', 'SECURITY'].map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-3 py-1.5 rounded-xl text-xs font-bold transition ${
                selectedCategory === cat
                  ? 'bg-purple-600 text-white shadow-md shadow-purple-600/30'
                  : 'bg-slate-900 text-slate-400 hover:text-slate-200 border border-slate-800'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </div>

      {/* Notices Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {filteredNotices.map((n) => (
          <div
            key={n.id}
            className={`p-6 rounded-2xl bg-slate-900 border shadow-xl space-y-3 relative ${
              n.isPinned ? 'border-purple-500/50 bg-gradient-to-br from-slate-900 via-slate-900 to-purple-950/40' : 'border-slate-800'
            }`}
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                {getCategoryIcon(n.category)}
                <span className="text-[10px] font-extrabold uppercase tracking-wider px-2 py-0.5 rounded bg-purple-500/20 text-purple-300">
                  {n.category}
                </span>
              </div>

              {n.isPinned && (
                <span className="flex items-center gap-1 text-[11px] font-extrabold text-amber-400 bg-amber-500/10 border border-amber-500/30 px-2.5 py-0.5 rounded-full">
                  <Pin className="w-3 h-3" /> Pinned
                </span>
              )}
            </div>

            <h3 className="font-extrabold text-lg text-white">{n.title}</h3>
            <p className="text-xs text-slate-300 leading-relaxed">{n.content}</p>

            <div className="pt-3 border-t border-slate-800 flex items-center justify-between text-[11px] text-slate-400">
              <span className="flex items-center gap-1">
                <Calendar className="w-3.5 h-3.5 text-slate-500" /> Published {new Date(n.publishDate).toLocaleDateString()}
              </span>
              <span className="font-semibold text-slate-400">By {n.adminName}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
