package com.nju.edu.cn.whispermusic.controller;

import com.nju.edu.cn.whispermusic.entity.Reply;
import com.nju.edu.cn.whispermusic.entity.Whisper;
import com.nju.edu.cn.whispermusic.service.FavoriteWhisperService;
import com.nju.edu.cn.whispermusic.service.ReplyService;
import com.nju.edu.cn.whispermusic.service.WhisperService;
import com.nju.edu.cn.whispermusic.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/whisper")
public class WhisperController {

    @Autowired
    private WhisperService whisperService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private FavoriteWhisperService favoriteWhisperService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String whisperListPage(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {
        Page<Whisper> whisperPage = whisperService.getWhisperList(page);
        model.addAttribute("page", whisperPage);
        return "whisperList";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String createWhisper(HttpSession session, String title, String content, boolean hasMusic, String musicId, String musicName) {
        Whisper whisper;
        if (hasMusic) {
            whisper = new Whisper(title, content, musicName, musicId);
        } else {
            whisper = new Whisper(title, content);
        }
        Long userId = (Long) session.getAttribute("userId");
        whisper = whisperService.createWhisper(userId, whisper);
        if (whisper != null) {
            return "redirect:/whisper/" + whisper.getId();
        } else {
            return "redirect:/whisper";
        }
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String createWhisperPage() {
        return "newWhisper";
    }

    @RequestMapping(value = "/new/{musicId}/{musicName}", method = RequestMethod.GET)
    public String musicCreateWhisperPage(HttpSession session, Model model, @PathVariable("musicId") String musicId, @PathVariable("musicName") String musicName) {
        String src = "//music.163.com/outchain/player?type=2&id=" + musicId + "&auto=0&height=66";
        model.addAttribute("musicId",musicId);
        model.addAttribute("musicName",musicName);
        model.addAttribute("src", src);
        return "newWhisper";
    }


    @RequestMapping(value = "/{whisperId}", method = RequestMethod.GET)
    public String whisperDetailPage(HttpSession session, Model model, @PathVariable("whisperId") Long whisperId) {
        Whisper whisper = whisperService.getWhisper(whisperId);
        model.addAttribute("whisper", whisper);
        Long userId = (Long) session.getAttribute("userId");
        boolean isFavoriteWhisperOfUser = favoriteWhisperService.isFavoriteWhisperOfUser(userId, whisperId);
        model.addAttribute("isFavoriteWhisperOfUser", isFavoriteWhisperOfUser);
        List<Reply> top5LikesReply = replyService.getTop5LikesReply(whisperId);
        model.addAttribute("top5LikesReply", top5LikesReply);
        List<Reply> stickedReply = replyService.getstickedReply(whisperId);
        model.addAttribute("stickedReply", stickedReply);
        return "whisperDetail";
    }

    @RequestMapping(value = "/{whisperId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response deleteWhisper(@PathVariable("whisperId") Long whisperId) {
        whisperService.deleteWhisper(whisperId);
        return new Response<>("OK!", "delete whisper-" + whisperId + " successfully.", "");
    }

    @RequestMapping(value = "/{whisperId}/favorite", method = RequestMethod.POST)
    @ResponseBody
    public Response favoriteWhisper(HttpSession session, @PathVariable("whisperId") Long whisperId) {
        Long userId = (Long) session.getAttribute("userId");
        favoriteWhisperService.favorite(userId, whisperId);
        return new Response<>("OK!", "user-" + userId + " favorite whisper-" + whisperId + " successfully.", "");
    }

    @RequestMapping(value = "/{whisperId}/unfavorite", method = RequestMethod.POST)
    @ResponseBody
    public Response unfavoriteWhisper(HttpSession session, @PathVariable("whisperId") Long whisperId) {
        Long userId = (Long) session.getAttribute("userId");
        favoriteWhisperService.unfavorite(userId, whisperId);
        return new Response<>("OK!", "user-" + userId + " unfavorite whisper-" + whisperId + " successfully.", "");
    }

    @RequestMapping(value = "/{whisperId}/like", method = RequestMethod.POST)
    @ResponseBody
    public Response likeWhisper(@PathVariable("whisperId") Long whisperId) {
        whisperService.addLikes(whisperId);
        return new Response<>("OK!", "add likes of whisper-" + whisperId + " successfully.", "");
    }

    @RequestMapping(value = "/{whisperId}/replies/", method = RequestMethod.POST)
    public String createReply(HttpSession session, @PathVariable("whisperId") Long whisperId, String content, boolean hasMusic, String musicId, String musicName) {
        Long userId = (Long) session.getAttribute("userId");
        Reply reply;
        if (hasMusic){
            reply=new Reply(content,musicName,musicId);
        }else{
            reply=new Reply(content);
        }
        reply = replyService.createReply(whisperId, userId, reply);
        return "redirect:/whisper/" + whisperId;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Page testWhisperListPage(Model model, @RequestParam(required = false, defaultValue = "1") Integer page) {
        Page<Whisper> whisperPage = whisperService.getWhisperList(page);
        return whisperPage;
    }

}
