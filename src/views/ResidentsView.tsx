import React, { useState } from 'react';
import { Building, Flat, Resident } from '../types';
import { Users, Building2, Search, Mail, Phone, Calendar } from 'lucide-react';

interface ResidentsViewProps {
  buildings: Building[];
  flats: Flat[];
  residents: Resident[];
}

export const ResidentsView: React.FC<ResidentsViewProps> = ({ buildings, flats, residents }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedBuilding, setSelectedBuilding] = useState<string>('ALL');

  const filteredResidents = residents.filter((r) => {
    const matchesSearch =
      r.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.flatNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      r.email.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesBuilding = selectedBuilding === 'ALL' || r.buildingName === selectedBuilding;
    return matchesSearch && matchesBuilding;
  });

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-slate-900 flex items-center gap-2">
            <Users className="w-6 h-6 text-indigo-600" /> Residents & Flat Directory
          </h2>
          <p className="text-xs text-slate-500 mt-1">Manage Aparna Cyber Heights residential blocks, flat allocations, and resident profiles</p>
        </div>

        {/* Search & Filter */}
        <div className="flex flex-wrap items-center gap-3">
          <div className="relative">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-3" />
            <input
              type="text"
              placeholder="Search resident name, flat, email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-9 pr-4 py-2 bg-white border border-slate-300 rounded-xl text-xs text-slate-900 focus:outline-none focus:border-indigo-600 w-64 shadow-sm"
            />
          </div>

          <select
            value={selectedBuilding}
            onChange={(e) => setSelectedBuilding(e.target.value)}
            className="px-3 py-2 bg-white border border-slate-300 rounded-xl text-xs text-slate-900 focus:outline-none focus:border-indigo-600 shadow-sm font-semibold"
          >
            <option value="ALL">All Blocks</option>
            {buildings.map((b) => (
              <option key={b.id} value={b.name}>
                {b.name}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Buildings Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {buildings.map((b) => (
          <div key={b.id} className="p-4 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-extrabold text-indigo-700 flex items-center gap-1.5">
                <Building2 className="w-4 h-4 text-indigo-600" /> {b.name}
              </span>
              <span className="text-[11px] font-bold px-2 py-0.5 rounded bg-indigo-50 text-indigo-700 border border-indigo-200">
                {b.totalFloors} Floors
              </span>
            </div>
            <p className="text-xs text-slate-500 font-medium">{b.description}</p>
            <div className="flex items-center justify-between pt-2 border-t border-slate-100 text-xs text-slate-700 font-semibold">
              <span>Flats: {b.totalFlats} Units</span>
              <span className="text-emerald-600 font-bold">{b.occupiedFlats} Occupied</span>
            </div>
          </div>
        ))}
      </div>

      {/* Resident Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {filteredResidents.map((r) => (
          <div key={r.id} className="p-5 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-4 relative hover:shadow-md transition">
            <div className="flex items-start justify-between">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-600 to-purple-600 flex items-center justify-center font-extrabold text-white text-base shadow-md shadow-indigo-500/20">
                  {r.firstName[0]}
                  {r.lastName[0]}
                </div>
                <div>
                  <h3 className="font-extrabold text-base text-slate-900">
                    {r.firstName} {r.lastName}
                  </h3>
                  <span
                    className={`text-[10px] font-extrabold px-2 py-0.5 rounded-full ${
                      r.residentType === 'OWNER' ? 'bg-amber-100 text-amber-800 border border-amber-200' : 'bg-blue-100 text-blue-800 border border-blue-200'
                    }`}
                  >
                    {r.residentType} {r.isPrimary ? '(Primary Owner)' : ''}
                  </span>
                </div>
              </div>

              <div className="text-right">
                <span className="text-xs font-extrabold text-blue-700 bg-blue-50 border border-blue-200 px-2.5 py-1 rounded-lg">
                  {r.flatNumber}
                </span>
                <p className="text-[10px] text-slate-500 font-medium mt-1">{r.buildingName}</p>
              </div>
            </div>

            <div className="space-y-1.5 pt-3 border-t border-slate-100 text-xs text-slate-600 font-medium">
              <div className="flex items-center space-x-2">
                <Mail className="w-3.5 h-3.5 text-slate-400" />
                <span>{r.email}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Phone className="w-3.5 h-3.5 text-slate-400" />
                <span>{r.phoneNumber}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Calendar className="w-3.5 h-3.5 text-slate-400" />
                <span>Move-in: {r.moveInDate}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
