package Services;

import utils.MyDb;
import Models.Reclamation;
import utils.UserSession;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements CrudInterface<Reclamation> {
    static Connection conn = MyDb.getMydb().getConnection();
    UserSession session = UserSession.getInstance();

    public static class Stats {
        public long totalCount;
        public long resolvedCount;
        public long pendingCount;
        public long rejectedCount;

        public Stats(long total, long resolved, long pending, long rejected) {
            this.totalCount = total;
            this.resolvedCount = resolved;
            this.pendingCount = pending;
            this.rejectedCount = rejected;
        }
    }

    public Stats getComplaintStats() {
        Stats stats = new Stats(0, 0, 0, 0);

        try {
            String query = "SELECT " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN status IS NULL THEN 1 ELSE 0 END) as pending, " +
                    "SUM(CASE WHEN status = true THEN 1 ELSE 0 END) as resolved, " +
                    "SUM(CASE WHEN status = false THEN 1 ELSE 0 END) as rejected " +
                    "FROM complaint";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                stats.totalCount = rs.getLong("total");
                stats.pendingCount = rs.getLong("pending");
                stats.resolvedCount = rs.getLong("resolved");
                stats.rejectedCount = rs.getLong("rejected");
            }
        } catch (SQLException e) {
            System.out.println("Error getting complaint stats: " + e.getMessage());
            throw new RuntimeException("Failed to get complaint statistics", e);
        }

        return stats;
    }

    public void add(Reclamation reclamation) {
        String query = "INSERT INTO complaint (user_id, subject, content, created_at) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reclamation.getUserId());
            ps.setString(2, reclamation.getSubject());
            ps.setString(3, reclamation.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(reclamation.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reclamation.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Complaint added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
            throw new RuntimeException("Failed to add complaint", e);
        }
    }

    public List<Reclamation> getAll() {
        List<Reclamation> complaints = new ArrayList<>();
        String query = "SELECT * FROM complaint ORDER BY created_at DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Reclamation complaint = new Reclamation();
                complaint.setId(rs.getInt("id"));
                complaint.setUserId(rs.getInt("user_id"));
                complaint.setRespondedById(rs.getInt("responded_by_id"));
                complaint.setSubject(rs.getString("subject"));
                complaint.setContent(rs.getString("content"));
                complaint.setStatus(rs.getBoolean("status"));
                if (rs.wasNull()) {
                    complaint.setStatus(null);
                }
                complaint.setResponse(rs.getString("response"));
                complaint.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
            throw new RuntimeException("Failed to get complaints", e);
        }
        return complaints;
    }

    public void update(Reclamation reclamation) {
        String query = "UPDATE complaint SET subject=?, content=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, reclamation.getSubject());
            ps.setString(2, reclamation.getContent());
            ps.setInt(3, reclamation.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("No complaint found with ID: " + reclamation.getId());
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
            throw new RuntimeException("Failed to update complaint", e);
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM complaint WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted == 0) {
                System.out.println("No complaint found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
            throw new RuntimeException("Failed to delete complaint", e);
        }
    }

    public Reclamation getById(int id) {
        Reclamation complaint = null;
        String query = "SELECT * FROM complaint WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                complaint = new Reclamation();
                complaint.setId(rs.getInt("id"));
                complaint.setUserId(rs.getInt("user_id"));
                complaint.setRespondedById(rs.getInt("responded_by_id"));
                complaint.setSubject(rs.getString("subject"));
                complaint.setContent(rs.getString("content"));
                complaint.setStatus(rs.getBoolean("status"));
                if (rs.wasNull()) {
                    complaint.setStatus(null);
                }
                complaint.setResponse(rs.getString("response"));
                complaint.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.out.println("Get Complaint by ID Error: " + e.getMessage());
            throw new RuntimeException("Failed to get complaint by ID", e);
        }
        return complaint;
    }

    @Override
    public void create(Reclamation obj) throws Exception {
        add(obj);
    }

    public void updateStatus(int id, Boolean status, String response, int respondedById) {
        String query = "UPDATE complaint SET status=?, response=?, responded_by_id=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            if (status != null) {
                ps.setBoolean(1, status);
            } else {
                ps.setNull(1, Types.BOOLEAN);
            }
            ps.setString(2, response);
            ps.setInt(3, respondedById);
            ps.setInt(4, id);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("No complaint found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Update Status Error: " + e.getMessage());
            throw new RuntimeException("Failed to update complaint status", e);
        }
    }
}