import React, { useState } from 'react';
import { MaintenanceRequest, Resident, Flat } from '../types';
import { Wrench, Plus, X } from 'lucide-react';
import { createMaintenanceApi } from '../api';

interface MaintenanceViewProps {
  maintenance: MaintenanceRequest[];
  residents: Resident[];
  flats: Flat[];
  onRefresh: () => void;
}

export const MaintenanceView: React.FC<MaintenanceViewProps> = ({ maintenance, residents, flats, onRefresh }) => {
  const [showModal, setShowModal] = useState(false);
  const [category, setCategory] = useState('Plumbing');
  const [priority, setPriority] = useState<any>('MEDIUM');
  const [description, setDescription] = useState('');
  const [selectedResidentId, setSelectedResidentId] = useState<number>(residents[0]?.id || 1);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const targetResident = residents.find((r) => r.id === Number(selectedResidentId)) || residents[0];
    await createMaintenanceApi({
      residentId: targetResident.id,
      flatId: targetResident.flatId,
      category,
      priority,
      description,
    });
    setShowModal(false);
    onRefresh();
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-slate-900 flex items-center gap-2">
            <Wrench className="w-6 h-6 text-amber-500" /> Maintenance & Specialized Staff Work Orders
          </h2>
          <p className="text-xs text-slate-500 mt-1">
            Assigned tasks for Plumber (Venkatesh Rao), Gardener (Ramesh Kumar), Interiors & Woodwork (Narsimha Chary)
          </p>
        </div>

        <button
          onClick={() => setShowModal(true)}
          className="px-4 py-2.5 rounded-xl bg-amber-500 hover:bg-amber-600 text-white text-xs font-extrabold transition flex items-center gap-2 shadow-lg shadow-amber-500/20"
        >
          <Plus className="w-4 h-4" /> Raise Maintenance Request
        </button>
      </div>

      {/* Work Orders List */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {maintenance.map((m) => (
          <div key={m.id} className="p-5 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-4 relative hover:shadow-md transition">
            <div className="flex items-start justify-between">
              <div>
                <span className="text-[10px] font-extrabold px-2 py-0.5 rounded uppercase tracking-wider bg-amber-100 text-amber-800">
                  {m.category}
                </span>
                <h3 className="font-extrabold text-base text-slate-900 mt-1">{m.description}</h3>
                <p className="text-xs text-slate-500 font-medium">
                  Flat {m.flatNumber} ({m.buildingName}) • {m.residentName}
                </p>
              </div>

              <span
                className={`text-[10px] font-extrabold px-2 py-0.5 rounded ${
                  m.priority === 'URGENT' || m.priority === 'HIGH'
                    ? 'bg-rose-100 text-rose-800 border border-rose-200'
                    : 'bg-slate-100 text-slate-700'
                }`}
              >
                {m.priority}
              </span>
            </div>

            <div className="space-y-1.5 text-xs text-slate-700 bg-slate-50 p-3 rounded-xl border border-slate-200 font-medium">
              <div className="flex justify-between">
                <span className="text-slate-500">Assigned Staff:</span>
                <span className="font-bold text-indigo-700">{m.assignedStaffName || 'Unassigned'}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-500">Raised On:</span>
                <span className="font-bold text-slate-800">{new Date(m.createdAt).toLocaleDateString()}</span>
              </div>
            </div>

            <div className="flex items-center justify-between pt-2 border-t border-slate-100">
              <span
                className={`text-[11px] font-bold px-2.5 py-1 rounded-full ${
                  m.status === 'COMPLETED'
                    ? 'bg-emerald-100 text-emerald-800 border border-emerald-200'
                    : m.status === 'IN_PROGRESS'
                    ? 'bg-blue-100 text-blue-800 border border-blue-200'
                    : 'bg-amber-100 text-amber-800 border border-amber-200'
                }`}
              >
                {m.status}
              </span>
              <span className="text-[11px] text-slate-400 font-mono font-bold">REQ-{m.id.toString().padStart(4, '0')}</span>
            </div>
          </div>
        ))}
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white border border-slate-200 rounded-2xl w-full max-w-md p-6 space-y-4 shadow-2xl relative">
            <button onClick={() => setShowModal(false)} className="absolute top-4 right-4 text-slate-400 hover:text-slate-800">
              <X className="w-5 h-5" />
            </button>
            <h3 className="font-extrabold text-lg text-slate-900 flex items-center gap-2">
              <Wrench className="w-5 h-5 text-amber-500" /> Raise Maintenance Request
            </h3>
            <form onSubmit={handleSubmit} className="space-y-3 text-xs font-medium">
              <div>
                <label className="block text-slate-700 mb-1 font-bold">Resident / Flat Location</label>
                <select
                  value={selectedResidentId}
                  onChange={(e) => setSelectedResidentId(Number(e.target.value))}
                  className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-amber-500 font-semibold"
                >
                  {residents.map((r) => (
                    <option key={r.id} value={r.id}>
                      {r.firstName} {r.lastName} - Flat {r.flatNumber}
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Work Category</label>
                  <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-amber-500 font-semibold"
                  >
                    <option value="Plumbing">Plumbing</option>
                    <option value="Gardening & Landscaping">Gardening & Landscaping</option>
                    <option value="Interiors & Woodwork">Interiors & Woodwork</option>
                    <option value="Electrical">Electrical</option>
                    <option value="HVAC">HVAC / Air Conditioning</option>
                  </select>
                </div>
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Priority Level</label>
                  <select
                    value={priority}
                    onChange={(e) => setPriority(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-amber-500"
                  >
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                    <option value="URGENT">Urgent</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-slate-700 mb-1 font-bold">Problem Description</label>
                <textarea
                  rows={3}
                  required
                  placeholder="Describe the repair needed..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-amber-500 resize-none"
                ></textarea>
              </div>

              <button
                type="submit"
                className="w-full mt-2 py-3 bg-amber-500 hover:bg-amber-600 text-white rounded-xl font-bold transition shadow-lg shadow-amber-500/20 text-xs"
              >
                Submit Request to Maintenance Team
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
