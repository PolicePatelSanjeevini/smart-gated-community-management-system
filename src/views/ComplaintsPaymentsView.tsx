import React, { useState } from 'react';
import { Complaint, Payment } from '../types';
import { CreditCard, AlertCircle, CheckCircle, DollarSign, ShieldCheck, X } from 'lucide-react';
import { processPaymentSimulatedApi } from '../api';

interface ComplaintsPaymentsViewProps {
  complaints: Complaint[];
  payments: Payment[];
  onRefresh: () => void;
}

export const ComplaintsPaymentsView: React.FC<ComplaintsPaymentsViewProps> = ({ complaints, payments, onRefresh }) => {
  const [selectedPayment, setSelectedPayment] = useState<Payment | null>(null);
  const [paymentMethod, setPaymentMethod] = useState<string>('SIMULATED_UPI');

  const handlePay = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedPayment) return;
    await processPaymentSimulatedApi(selectedPayment.id, paymentMethod);
    setSelectedPayment(null);
    onRefresh();
  };

  return (
    <div className="space-y-8">
      {/* Section 1: Maintenance Charge Payments */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-xl font-extrabold text-white flex items-center gap-2">
              <CreditCard className="w-6 h-6 text-rose-400" /> Maintenance Billing & Dues
            </h2>
            <p className="text-xs text-slate-400 mt-1">Monthly community maintenance fees, clubhouse charges, and payment records</p>
          </div>
          <span className="text-xs font-bold text-slate-400 bg-slate-900 border border-slate-800 px-3 py-1.5 rounded-xl">
            Development Payment Simulator Ready
          </span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {payments.map((p) => (
            <div key={p.id} className="p-5 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl space-y-4 relative">
              <div className="flex items-start justify-between">
                <div>
                  <span className="text-[10px] font-extrabold px-2 py-0.5 rounded uppercase tracking-wider bg-rose-500/20 text-rose-300">
                    {p.feeType.replace('_', ' ')}
                  </span>
                  <h3 className="font-extrabold text-xl text-white mt-1">${p.amount.toFixed(2)}</h3>
                  <p className="text-xs text-slate-400">
                    Flat {p.flatNumber} ({p.buildingName}) • {p.residentName}
                  </p>
                </div>

                <span
                  className={`text-[11px] font-bold px-2.5 py-1 rounded-full ${
                    p.paymentStatus === 'PAID'
                      ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/30'
                      : 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
                  }`}
                >
                  {p.paymentStatus}
                </span>
              </div>

              <div className="space-y-1 text-xs text-slate-400 bg-slate-800/40 p-3 rounded-xl border border-slate-800">
                <div className="flex justify-between">
                  <span>Due Date:</span>
                  <span className="font-semibold text-slate-200">{p.dueDate}</span>
                </div>
                {p.transactionRef && (
                  <div className="flex justify-between">
                    <span>Transaction Ref:</span>
                    <span className="font-mono text-emerald-400 font-bold">{p.transactionRef}</span>
                  </div>
                )}
              </div>

              {p.paymentStatus === 'PENDING' && (
                <button
                  onClick={() => setSelectedPayment(p)}
                  className="w-full py-2.5 bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white rounded-xl text-xs font-bold transition shadow-lg shadow-emerald-600/30 flex items-center justify-center gap-1.5"
                >
                  <DollarSign className="w-4 h-4" /> Pay Bill (Development Simulator)
                </button>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Section 2: Resident Complaints */}
      <div className="space-y-4 pt-6 border-t border-slate-800">
        <div>
          <h2 className="text-xl font-extrabold text-white flex items-center gap-2">
            <AlertCircle className="w-6 h-6 text-amber-400" /> Resident Complaints Tracker
          </h2>
          <p className="text-xs text-slate-400 mt-1">Community grievances, noise reports, and resolution logs</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {complaints.map((c) => (
            <div key={c.id} className="p-5 rounded-2xl bg-slate-900 border border-slate-800 shadow-xl space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-[10px] font-extrabold px-2 py-0.5 rounded uppercase tracking-wider bg-amber-500/20 text-amber-300">
                  {c.category}
                </span>
                <span
                  className={`text-[11px] font-bold px-2.5 py-0.5 rounded-full ${
                    c.status === 'RESOLVED'
                      ? 'bg-emerald-500/20 text-emerald-300'
                      : 'bg-amber-500/20 text-amber-300'
                  }`}
                >
                  {c.status}
                </span>
              </div>
              <h3 className="font-extrabold text-base text-white">{c.title}</h3>
              <p className="text-xs text-slate-300">{c.description}</p>
              <div className="pt-2 border-t border-slate-800 flex justify-between text-xs text-slate-400">
                <span>By {c.residentName} (Flat {c.flatNumber})</span>
                <span>{new Date(c.createdAt).toLocaleDateString()}</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Simulated Payment Modal */}
      {selectedPayment && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-md p-6 space-y-4 shadow-2xl relative">
            <button onClick={() => setSelectedPayment(null)} className="absolute top-4 right-4 text-slate-400 hover:text-white">
              <X className="w-5 h-5" />
            </button>
            <h3 className="font-extrabold text-lg text-white flex items-center gap-2">
              <CreditCard className="w-5 h-5 text-emerald-400" /> Simulated Payment Portal
            </h3>
            <div className="bg-slate-800/60 p-3.5 rounded-xl border border-slate-700 space-y-1 text-xs">
              <div className="flex justify-between text-slate-300">
                <span>Flat & Resident:</span>
                <span className="font-bold text-white">Flat {selectedPayment.flatNumber} - {selectedPayment.residentName}</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Fee Type:</span>
                <span className="font-bold text-white">{selectedPayment.feeType}</span>
              </div>
              <div className="flex justify-between text-base font-extrabold text-emerald-400 pt-1">
                <span>Amount Due:</span>
                <span>${selectedPayment.amount.toFixed(2)}</span>
              </div>
            </div>

            <form onSubmit={handlePay} className="space-y-3 text-xs">
              <div>
                <label className="block text-slate-400 mb-1 font-semibold">Select Payment Method</label>
                <select
                  value={paymentMethod}
                  onChange={(e) => setPaymentMethod(e.target.value)}
                  className="w-full bg-slate-800 border border-slate-700 rounded-xl p-2.5 text-white focus:outline-none focus:border-emerald-500"
                >
                  <option value="SIMULATED_UPI">UPI (Google Pay / PhonePe Simulator)</option>
                  <option value="SIMULATED_CARD">Credit / Debit Card Simulator</option>
                  <option value="SIMULATED_NETBANKING">NetBanking Simulator</option>
                </select>
              </div>

              <div className="p-3 bg-amber-500/10 border border-amber-500/30 rounded-xl text-[11px] text-amber-300 leading-tight">
                <strong>Note:</strong> This is a development payment simulator. Clicking Pay will record a simulated transaction ref without charging real money.
              </div>

              <button
                type="submit"
                className="w-full py-3 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl font-bold transition shadow-lg shadow-emerald-600/30"
              >
                Confirm Simulated Payment (${selectedPayment.amount.toFixed(2)})
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
