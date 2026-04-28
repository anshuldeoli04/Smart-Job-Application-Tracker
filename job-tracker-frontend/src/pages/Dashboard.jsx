import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/Api';

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [statsRes, jobsRes] = await Promise.all([
        API.get('/dashboard'),
        API.get('/jobs'),
      ]);
      setStats(statsRes.data);
      setJobs(jobsRes.data);
    } catch {
      localStorage.removeItem('token');
      navigate('/');
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const statusColor = (status) => {
    const colors = {
      APPLIED: 'bg-blue-100 text-blue-700',
      SHORTLISTED: 'bg-yellow-100 text-yellow-700',
      INTERVIEW: 'bg-purple-100 text-purple-700',
      OFFER: 'bg-green-100 text-green-700',
      REJECTED: 'bg-red-100 text-red-700',
    };
    return colors[status] || 'bg-gray-100 text-gray-700';
  };

  if (loading) return (
    <div className="min-h-screen flex items-center justify-center">
      <p className="text-gray-500">Loading...</p>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar */}
      <nav className="bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
        <h1 className="text-lg font-semibold text-gray-800">Job Tracker</h1>
        <div className="flex gap-3">
          <button
            onClick={() => navigate('/add-job')}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-700 transition"
          >
            + Add Job
          </button>
          <button
            onClick={logout}
            className="text-sm text-gray-500 hover:text-gray-700 px-4 py-2 rounded-lg border border-gray-200 hover:bg-gray-50 transition"
          >
            Logout
          </button>
          <button
            onClick={() => navigate('/ai-match')}
            className="bg-purple-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-purple-700 transition"
          >
            AI Match
          </button>
        </div>
      </nav>

      <div className="max-w-5xl mx-auto px-6 py-8">
        {/* Stats Cards */}
        {stats && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
            {[
              { label: 'Total Applied', value: stats.totalApplications, color: 'text-blue-600' },
              { label: 'Interviews', value: stats.interview, color: 'text-purple-600' },
              { label: 'Offers', value: stats.offer, color: 'text-green-600' },
              { label: 'Rejected', value: stats.rejected, color: 'text-red-500' },
            ].map((s) => (
              <div key={s.label} className="bg-white rounded-xl p-5 shadow-sm border border-gray-100">
                <p className="text-sm text-gray-500 mb-1">{s.label}</p>
                <p className={`text-3xl font-semibold ${s.color}`}>{s.value}</p>
              </div>
            ))}
          </div>
        )}

        {/* Interview & Offer Rate */}
        {stats && (
          <div className="grid grid-cols-2 gap-4 mb-8">
            <div className="bg-white rounded-xl p-5 shadow-sm border border-gray-100">
              <p className="text-sm text-gray-500 mb-1">Interview Rate</p>
              <p className="text-2xl font-semibold text-gray-800">{stats.interviewRate}%</p>
              <div className="mt-2 h-2 bg-gray-100 rounded-full">
                <div className="h-2 bg-purple-500 rounded-full" style={{ width: `${stats.interviewRate}%` }}></div>
              </div>
            </div>
            <div className="bg-white rounded-xl p-5 shadow-sm border border-gray-100">
              <p className="text-sm text-gray-500 mb-1">Offer Rate</p>
              <p className="text-2xl font-semibold text-gray-800">{stats.offerRate}%</p>
              <div className="mt-2 h-2 bg-gray-100 rounded-full">
                <div className="h-2 bg-green-500 rounded-full" style={{ width: `${stats.offerRate}%` }}></div>
              </div>
            </div>
          </div>
        )}

        {/* Jobs List */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100">
          <div className="px-6 py-4 border-b border-gray-100">
            <h2 className="font-medium text-gray-800">Applications ({jobs.length})</h2>
          </div>
          {jobs.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-400 mb-4">No applications yet</p>
              <button
                onClick={() => navigate('/add-job')}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-700"
              >
                Add your first job
              </button>
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {jobs.map((job) => (
                <div key={job.id} className="px-6 py-4 flex items-center justify-between hover:bg-gray-50 transition">
                  <div>
                    <p className="font-medium text-gray-800">{job.companyName}</p>
                    <p className="text-sm text-gray-500">{job.role}</p>
                    <p className="text-xs text-gray-400 mt-1">{job.appliedDate}</p>
                  </div>
                  <div className="flex items-center gap-3">
                    {job.salaryRange && (
                      <span className="text-sm text-gray-500">{job.salaryRange}</span>
                    )}
                    <span className={`text-xs px-3 py-1 rounded-full font-medium ${statusColor(job.status)}`}>
                      {job.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}