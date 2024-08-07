package kr.jay.enverspilot.board.interfaces.dto;

import java.util.List;
import java.util.stream.Collectors;
import kr.jay.enverspilot.board.domain.Board;
import kr.jay.enverspilot.board.domain.reply.Reply;
import lombok.Builder;

@Builder
public record BoardResponse(
    Long id,
    String title,
    String content,
    String sub,
    List<ReplyResponse> replies
) {

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
            .id(board.getId())
            .title(board.getTitle())
            .content(board.getContent())
            .sub(board.getSub())
            .replies(convertToResponse(board.getReplies()))
            .build();
    }

    private static List<ReplyResponse> convertToResponse(List<Reply> replies) {
        return replies.stream()
            .map(ReplyResponse::from)
            .collect(Collectors.toList());
    }
}
