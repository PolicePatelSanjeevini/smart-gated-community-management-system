import React, { useState } from 'react';
import { Visitor, Resident, Flat } from '../types';
import { UserCheck, Plus, CheckCircle, LogOut, X } from 'lucide-react';
import { registerVisitorApi, checkInVisitorApi, checkOutVisitorApi } from '../api';

interface VisitorsViewProps {
  visitors: Visitor[];
  residents: Resident[];
  flats: Flat[];
  onRefresh: () => void;
}

export const VisitorsView: React.FC<VisitorsViewProps> = ({ visitors, residents, flats, onRefresh }) => {
  const [showModal, setShowModal] = useState(false);
  const [visitorName, setVisitorName] = useState('');
  const [phone, setPhone] = useState('');
  const [vehicle, setVehicle] = useState('');
  const [purpose, setPurpose] = useState('');
  const [type, setType] = useState<any>('GUEST');
  const [selectedResidentId, setSelectedResidentId] = useState<number>(residents[0]?.id || 1);

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    const targetResident = residents.find((r) => r.id === Number(selectedResidentId)) || residents[0];
    await registerVisitorApi({
      residentId: targetResident.id,
      flatId: targetResident.flatId,
      name: visitorName,
      phoneNumber: phone,
      vehicleNumber: vehicle,
      purpose,
      visitorType: type,
      expectedArrival: new Date(Date.now() + 3600000).toISOString(),
    });
    setShowModal(false);
    onRefresh();
  };

  const handleCheckIn = async (code: string) => {
    await checkInVisitorApi(code);
    onRefresh();
  };

  const handleCheckOut = async (code: string) => {
    await checkOutVisitorApi(code);
    onRefresh();
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-slate-900 flex items-center gap-2">
            <UserCheck className="w-6 h-6 text-emerald-600" /> Visitor & Gate Security Verification
          </h2>
          <p className="text-xs text-slate-500 mt-1">Pre-register guests, verify gate entries, and track vehicle passes for Gachibowli Gate</p>
        </div>

        <button
          onClick={() => setShowModal(true)}
          className="px-4 py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white text-xs font-extrabold transition flex items-center gap-2 shadow-lg shadow-emerald-500/20"
        >
          <Plus className="w-4 h-4" /> Pre-Register Visitor
        </button>
      </div>

      {/* Visitor Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {visitors.map((v) => (
          <div key={v.id} className="p-5 rounded-2xl bg-white border border-slate-200 shadow-sm space-y-4 relative hover:shadow-md transition">
            <div className="flex items-start justify-between">
              <div>
                <span className="text-[10px] font-extrabold px-2 py-0.5 rounded uppercase tracking-wider bg-emerald-100 text-emerald-800">
                  {v.visitorType}
                </span>
                <h3 className="font-extrabold text-base text-slate-900 mt-1">{v.name}</h3>
                <p className="text-xs text-slate-500 font-medium">
                  Visiting: <span className="text-slate-800 font-bold">{v.residentName}</span> ({v.flatNumber})
                </p>
              </div>

              <div className="text-right">
                <span className="text-xs font-extrabold px-2.5 py-1 rounded-lg bg-blue-50 text-blue-700 border border-blue-200">
                  {v.accessCode}
                </span>
              </div>
            </div>

            <div className="space-y-1.5 text-xs text-slate-700 bg-slate-50 p-3 rounded-xl border border-slate-200 font-medium">
              <div className="flex justify-between">
                <span className="text-slate-500">Purpose:</span>
                <span className="font-bold text-slate-800">{v.purpose}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-500">Vehicle:</span>
                <span className="font-bold text-slate-800">{v.vehicleNumber || 'On Foot'}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-slate-500">Phone:</span>
                <span className="font-bold text-slate-800">{v.phoneNumber}</span>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex items-center justify-between pt-2">
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

              {v.status === 'PRE_REGISTERED' && (
                <button
                  onClick={() => handleCheckIn(v.accessCode)}
                  className="px-3 py-1.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg text-xs font-bold transition flex items-center gap-1 shadow-md shadow-emerald-500/20"
                >
                  <CheckCircle className="w-3.5 h-3.5" /> Check-In Gate
                </button>
              )}

              {v.status === 'INSIDE' && (
                <button
                  onClick={() => handleCheckOut(v.accessCode)}
                  className="px-3 py-1.5 bg-rose-600 hover:bg-rose-700 text-white rounded-lg text-xs font-bold transition flex items-center gap-1 shadow-md shadow-rose-500/20"
                >
                  <LogOut className="w-3.5 h-3.5" /> Check-Out Gate
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Pre-Register Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white border border-slate-200 rounded-2xl w-full max-w-md p-6 space-y-4 shadow-2xl relative">
            <button onClick={() => setShowModal(false)} className="absolute top-4 right-4 text-slate-400 hover:text-slate-800">
              <X className="w-5 h-5" />
            </button>
            <h3 className="font-extrabold text-lg text-slate-900 flex items-center gap-2">
              <UserCheck className="w-5 h-5 text-emerald-600" /> Pre-Register Expected Visitor
            </h3>
            <form onSubmit={handleRegister} className="space-y-3 text-xs font-medium">
              <div>
                <label className="block text-slate-700 mb-1 font-bold">Select Resident / Flat</label>
                <select
                  value={selectedResidentId}
                  onChange={(e) => setSelectedResidentId(Number(e.target.value))}
                  className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600 font-semibold"
                >
                  {residents.map((r) => (
                    <option key={r.id} value={r.id}>
                      {r.firstName} {r.lastName} - Flat {r.flatNumber}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-slate-700 mb-1 font-bold">Visitor Full Name</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Raghavendra Rao"
                  value={visitorName}
                  onChange={(e) => setVisitorName(e.target.value)}
                  className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Phone Number</label>
                  <input
                    type="text"
                    required
                    placeholder="+91 94400 00000"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600"
                  />
                </div>
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Vehicle Number</label>
                  <input
                    type="text"
                    placeholder="e.g. TS 07 EA 9842"
                    value={vehicle}
                    onChange={(e) => setVehicle(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Visitor Category</label>
                  <select
                    value={type}
                    onChange={(e) => setType(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600"
                  >
                    <option value="GUEST">Guest</option>
                    <option value="DELIVERY">Delivery</option>
                    <option value="SERVICE_PROVIDER">Service Provider</option>
                    <option value="CAB">Cab</option>
                  </select>
                </div>
                <div>
                  <label className="block text-slate-700 mb-1 font-bold">Purpose of Visit</label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Family Lunch"
                    value={purpose}
                    onChange={(e) => setPurpose(e.target.value)}
                    className="w-full bg-white border border-slate-300 rounded-xl p-2.5 text-slate-900 focus:outline-none focus:border-emerald-600"
                  />
                </div>
              </div>

              <button
                type="submit"
                className="w-full mt-2 py-3 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl font-bold transition shadow-lg shadow-emerald-600/20 text-xs"
              >
                Generate Gate Access Code
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
