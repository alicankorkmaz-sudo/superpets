import { useState, useEffect } from 'react';
import { api } from '../lib/api';
import {
  Users,
  CreditCard,
  Image,
  TrendingUp,
  Activity,
  DollarSign,
  Calendar,
  Clock,
  Shield,
  ChevronDown,
  ChevronUp,
  Search,
  Filter,
} from 'lucide-react';

interface AdminStats {
  totalUsers: number;
  totalCreditsDistributed: number;
  totalEdits: number;
  totalRevenue: number;
  activeUsersToday: number;
  activeUsersWeek: number;
  editsToday: number;
  editsWeek: number;
}

interface AdminUserDetails {
  uid: string;
  email: string;
  credits: number;
  createdAt: number;
  isAdmin: boolean;
  totalEdits: number;
  totalCreditsUsed: number;
  totalCreditsPurchased: number;
  lastActivity: number | null;
}

interface Transaction {
  userId: string;
  amount: number;
  type: string;
  description: string;
  timestamp: number;
}

export function AdminDashboardPage() {
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [users, setUsers] = useState<AdminUserDetails[]>([]);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<'overview' | 'users' | 'transactions'>('overview');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedUser, setSelectedUser] = useState<AdminUserDetails | null>(null);
  const [showUserModal, setShowUserModal] = useState(false);
  const [sortBy, setSortBy] = useState<'createdAt' | 'credits' | 'totalEdits' | 'lastActivity'>('createdAt');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [statsData, usersData, transactionsData] = await Promise.all([
        api.getAdminStats(),
        api.getAdminUsers(100, 0),
        api.getAdminTransactions(50, 0),
      ]);
      setStats(statsData);
      setUsers(usersData.users);
      setTransactions(transactionsData.transactions);
    } catch (err: any) {
      setError(err.message || 'Failed to load admin data');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateUser = async (userId: string, updates: { isAdmin?: boolean; credits?: number }) => {
    try {
      await api.updateUser(userId, updates);
      await loadDashboardData();
      setShowUserModal(false);
    } catch (err: any) {
      alert('Failed to update user: ' + err.message);
    }
  };

  const formatDate = (timestamp: number) => {
    return new Date(timestamp).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const formatRelativeTime = (timestamp: number | null) => {
    if (!timestamp) return 'Never';
    const diff = Date.now() - timestamp;
    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(diff / 3600000);
    const days = Math.floor(diff / 86400000);

    if (minutes < 60) return `${minutes}m ago`;
    if (hours < 24) return `${hours}h ago`;
    return `${days}d ago`;
  };

  const filteredUsers = users
    .filter((user) =>
      user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      user.uid.toLowerCase().includes(searchTerm.toLowerCase())
    )
    .sort((a, b) => {
      const aVal = a[sortBy] ?? 0;
      const bVal = b[sortBy] ?? 0;
      return sortOrder === 'asc' ? (aVal > bVal ? 1 : -1) : (aVal < bVal ? 1 : -1);
    });

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-purple-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading admin dashboard...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50 flex items-center justify-center">
        <div className="bg-white p-8 rounded-lg shadow-lg max-w-md">
          <Shield className="w-16 h-16 text-red-500 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-gray-800 mb-2 text-center">Access Denied</h2>
          <p className="text-gray-600 text-center">{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-blue-50">
      <div className="max-w-7xl mx-auto p-6">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center gap-3 mb-2">
            <Shield className="w-8 h-8 text-purple-600" />
            <h1 className="text-4xl font-bold text-gray-800">Admin Dashboard</h1>
          </div>
          <p className="text-gray-600">Manage users, monitor activity, and view analytics</p>
        </div>

        {/* Tabs */}
        <div className="flex gap-2 mb-6">
          <button
            onClick={() => setActiveTab('overview')}
            className={`px-6 py-3 rounded-lg font-medium transition-all ${
              activeTab === 'overview'
                ? 'bg-white text-purple-600 shadow-md'
                : 'bg-white/50 text-gray-600 hover:bg-white/80'
            }`}
          >
            <Activity className="w-5 h-5 inline mr-2" />
            Overview
          </button>
          <button
            onClick={() => setActiveTab('users')}
            className={`px-6 py-3 rounded-lg font-medium transition-all ${
              activeTab === 'users'
                ? 'bg-white text-purple-600 shadow-md'
                : 'bg-white/50 text-gray-600 hover:bg-white/80'
            }`}
          >
            <Users className="w-5 h-5 inline mr-2" />
            Users ({users.length})
          </button>
          <button
            onClick={() => setActiveTab('transactions')}
            className={`px-6 py-3 rounded-lg font-medium transition-all ${
              activeTab === 'transactions'
                ? 'bg-white text-purple-600 shadow-md'
                : 'bg-white/50 text-gray-600 hover:bg-white/80'
            }`}
          >
            <CreditCard className="w-5 h-5 inline mr-2" />
            Transactions
          </button>
        </div>

        {/* Overview Tab */}
        {activeTab === 'overview' && stats && (
          <div className="space-y-6">
            {/* Stats Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <StatCard
                icon={<Users className="w-8 h-8 text-blue-500" />}
                title="Total Users"
                value={stats.totalUsers.toLocaleString()}
                subtitle={`${stats.activeUsersWeek} active this week`}
                bgGradient="from-blue-500 to-blue-600"
              />
              <StatCard
                icon={<Image className="w-8 h-8 text-purple-500" />}
                title="Total Edits"
                value={stats.totalEdits.toLocaleString()}
                subtitle={`${stats.editsToday} today`}
                bgGradient="from-purple-500 to-purple-600"
              />
              <StatCard
                icon={<CreditCard className="w-8 h-8 text-green-500" />}
                title="Credits Distributed"
                value={stats.totalCreditsDistributed.toLocaleString()}
                subtitle="All-time total"
                bgGradient="from-green-500 to-green-600"
              />
              <StatCard
                icon={<DollarSign className="w-8 h-8 text-yellow-500" />}
                title="Total Revenue"
                value={`${stats.totalRevenue.toLocaleString()} credits`}
                subtitle="From purchases"
                bgGradient="from-yellow-500 to-yellow-600"
              />
            </div>

            {/* Activity Stats */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-white rounded-lg shadow-md p-6">
                <h3 className="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
                  <Activity className="w-5 h-5 text-purple-600" />
                  User Activity
                </h3>
                <div className="space-y-3">
                  <ActivityRow
                    label="Active Today"
                    value={stats.activeUsersToday}
                    total={stats.totalUsers}
                    color="bg-green-500"
                  />
                  <ActivityRow
                    label="Active This Week"
                    value={stats.activeUsersWeek}
                    total={stats.totalUsers}
                    color="bg-blue-500"
                  />
                </div>
              </div>

              <div className="bg-white rounded-lg shadow-md p-6">
                <h3 className="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-purple-600" />
                  Edit Activity
                </h3>
                <div className="space-y-3">
                  <ActivityRow
                    label="Edits Today"
                    value={stats.editsToday}
                    total={stats.totalEdits}
                    color="bg-purple-500"
                  />
                  <ActivityRow
                    label="Edits This Week"
                    value={stats.editsWeek}
                    total={stats.totalEdits}
                    color="bg-indigo-500"
                  />
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Users Tab */}
        {activeTab === 'users' && (
          <div className="bg-white rounded-lg shadow-md">
            {/* Search and Filter */}
            <div className="p-4 border-b border-gray-200">
              <div className="flex gap-4 items-center">
                <div className="flex-1 relative">
                  <Search className="w-5 h-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                  <input
                    type="text"
                    placeholder="Search by email or user ID..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                  />
                </div>
                <div className="flex gap-2">
                  <select
                    value={sortBy}
                    onChange={(e) => setSortBy(e.target.value as any)}
                    className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                  >
                    <option value="createdAt">Date Joined</option>
                    <option value="credits">Credits</option>
                    <option value="totalEdits">Total Edits</option>
                    <option value="lastActivity">Last Active</option>
                  </select>
                  <button
                    onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}
                    className="px-3 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
                  >
                    {sortOrder === 'asc' ? <ChevronUp className="w-5 h-5" /> : <ChevronDown className="w-5 h-5" />}
                  </button>
                </div>
              </div>
            </div>

            {/* Users Table */}
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 border-b border-gray-200">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      User
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Credits
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Edits
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Last Active
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Joined
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredUsers.map((user) => (
                    <tr key={user.uid} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div>
                            <div className="text-sm font-medium text-gray-900 flex items-center gap-2">
                              {user.email}
                              {user.isAdmin && (
                                <span className="px-2 py-1 text-xs bg-purple-100 text-purple-700 rounded-full">
                                  Admin
                                </span>
                              )}
                            </div>
                            <div className="text-xs text-gray-500">{user.uid.substring(0, 16)}...</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{user.credits}</div>
                        <div className="text-xs text-gray-500">
                          {user.totalCreditsPurchased} purchased
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm text-gray-900">{user.totalEdits}</div>
                        <div className="text-xs text-gray-500">
                          {user.totalCreditsUsed} credits used
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {formatRelativeTime(user.lastActivity)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {formatDate(user.createdAt)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => {
                            setSelectedUser(user);
                            setShowUserModal(true);
                          }}
                          className="text-purple-600 hover:text-purple-900"
                        >
                          Manage
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Transactions Tab */}
        {activeTab === 'transactions' && (
          <div className="bg-white rounded-lg shadow-md">
            <div className="p-6">
              <h3 className="text-lg font-semibold text-gray-800 mb-4">Recent Transactions</h3>
              <div className="space-y-2">
                {transactions.map((tx, idx) => (
                  <div
                    key={idx}
                    className="flex items-center justify-between p-4 bg-gray-50 rounded-lg hover:bg-gray-100"
                  >
                    <div className="flex-1">
                      <div className="text-sm font-medium text-gray-900">{tx.description}</div>
                      <div className="text-xs text-gray-500">User: {tx.userId.substring(0, 16)}...</div>
                    </div>
                    <div className="text-right">
                      <div
                        className={`text-sm font-semibold ${
                          tx.amount > 0 ? 'text-green-600' : 'text-red-600'
                        }`}
                      >
                        {tx.amount > 0 ? '+' : ''}
                        {tx.amount} credits
                      </div>
                      <div className="text-xs text-gray-500">{formatDate(tx.timestamp)}</div>
                    </div>
                    <div className="ml-4">
                      <span className="px-2 py-1 text-xs bg-gray-200 text-gray-700 rounded">
                        {tx.type}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>

      {/* User Management Modal */}
      {showUserModal && selectedUser && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-xl font-bold text-gray-800 mb-4">Manage User</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <div className="text-sm text-gray-900">{selectedUser.email}</div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Current Credits</label>
                <div className="text-sm text-gray-900">{selectedUser.credits}</div>
              </div>
              <div>
                <label className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    checked={selectedUser.isAdmin}
                    onChange={(e) => handleUpdateUser(selectedUser.uid, { isAdmin: e.target.checked })}
                    className="rounded border-gray-300 text-purple-600 focus:ring-purple-500"
                  />
                  <span className="text-sm font-medium text-gray-700">Admin User</span>
                </label>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Update Credits</label>
                <div className="flex gap-2">
                  <input
                    type="number"
                    defaultValue={selectedUser.credits}
                    className="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                    onKeyDown={(e) => {
                      if (e.key === 'Enter') {
                        const input = e.target as HTMLInputElement;
                        handleUpdateUser(selectedUser.uid, { credits: parseInt(input.value) });
                      }
                    }}
                  />
                  <button
                    onClick={(e) => {
                      const input = (e.target as HTMLButtonElement).previousElementSibling as HTMLInputElement;
                      handleUpdateUser(selectedUser.uid, { credits: parseInt(input.value) });
                    }}
                    className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700"
                  >
                    Update
                  </button>
                </div>
              </div>
              <div className="pt-4 flex gap-2">
                <button
                  onClick={() => setShowUserModal(false)}
                  className="flex-1 px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

function StatCard({ icon, title, value, subtitle, bgGradient }: any) {
  return (
    <div className="bg-white rounded-lg shadow-md p-6 relative overflow-hidden">
      <div className={`absolute top-0 right-0 w-24 h-24 bg-gradient-to-br ${bgGradient} opacity-10 rounded-bl-full`}></div>
      <div className="relative">
        <div className="flex items-center justify-between mb-2">
          {icon}
        </div>
        <div className="text-3xl font-bold text-gray-800 mb-1">{value}</div>
        <div className="text-sm text-gray-600">{title}</div>
        <div className="text-xs text-gray-500 mt-1">{subtitle}</div>
      </div>
    </div>
  );
}

function ActivityRow({ label, value, total, color }: any) {
  const percentage = Math.round((value / total) * 100);
  return (
    <div>
      <div className="flex justify-between text-sm mb-1">
        <span className="text-gray-700">{label}</span>
        <span className="font-semibold text-gray-900">{value.toLocaleString()}</span>
      </div>
      <div className="w-full bg-gray-200 rounded-full h-2">
        <div className={`${color} h-2 rounded-full`} style={{ width: `${percentage}%` }}></div>
      </div>
    </div>
  );
}
